package com.salat.suggester;

import com.salat.suggester.parsers.spotbugs.SpotBugsParser;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.exceptions.ToolInvocationException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Mojo(name = "suggest")
public class SuggesterMojo extends AbstractMavenReport {
    /**
     * Location of file, that contains bugs messages from code analysers, for example, spotbugsXml.xml.
     */
    @Parameter(property = "inputFileWithBugs", defaultValue = "${project.build.directory}/spotbugsXml.xml")
    private File inputFileWithBugs;

    /**
     * Address of Ollama server
     */
    @Parameter(property = "ollamaHost", defaultValue = "http://localhost:11434")
    private String ollamaHost;

    /**
     * Username to log in to Ollama server
     */
    @Parameter(property = "ollamaUsername")
    private String ollamaUsername;

    /**
     * Password to log in to Ollama server
     */
    @Parameter(property = "ollamaPassword")
    private String ollamaPassword;

    /**
     * Request timeout to model in seconds. Default is 600 seconds.
     */
    @Parameter(property = "modelRequestTimeout", defaultValue = "600")
    private long modelRequestTimeout;

    /**
     * Model from Ollama to use for generation suggestions.
     */
    @Parameter(property = "modelName", required = true)
    private String modelName;

    /**
     * Prompt for specified model.
     * <br><br>
     * Supports arguments placement:
     * <ul>
     *     <li><pre>%bugContent%</pre> will be replaced by actual bug description</li>
     * </ul>
     */
    @Parameter(property = "prompt", required = true)
    private String prompt;

    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File sourceProjectBuildDirectory;

    @Parameter(property = "suggestionsOutputFilename", defaultValue = "bugfix-suggestions.xml")
    private String suggestionsOutputFilename;

    @Parameter(defaultValue = "${project.name}", required = true)
    private String sourceProjectName;

    @Parameter(defaultValue = "${project.version}", required = true)
    private String sourceProjectVersion;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true)
    private File sourceDirectory;

    private Map<BugEntity, SuggestionEntity> bugfixes = new HashMap<>();

    @Override
    public boolean canGenerateReport() throws MavenReportException {
        List<BugEntity> bugs;
        {
            try {
                bugs = parseFileAndCollectBugs();
            } catch (Exception e) {
                throw new MavenReportException("Error parsing bugs from file", e);
            }
        }
        bugs.stream().map(BugEntity::getContent).forEach(
                s -> getLog().info("Bug: " + s)
        );

        {
            try {
                bugfixes = suggestBugfixes(bugs);
            } catch (Exception e) {
                throw new MavenReportException("Error making API call to ollama", e);
            }
        }

        for (Map.Entry<BugEntity, SuggestionEntity> bugfix : bugfixes.entrySet()) {
            BugEntity bug = bugfix.getKey();
            SuggestionEntity suggestion = bugfix.getValue();
            getLog().info("Bug: " + bug.getContent());
            getLog().info("Suggestion: " + suggestion.getContent());
        }

        {
            try {
                dumpBugfixesToOutputFile();
            } catch (Exception e) {
                getLog().error(e.toString());
                throw new MavenReportException("Failed to dump bugfix suggestions to file", e);
            }
        }

        return !bugfixes.isEmpty();
    }

    private List<BugEntity> parseFileAndCollectBugs() throws Exception {
        if (inputFileWithBugs.exists()) {
            return new SpotBugsParser().parse(inputFileWithBugs);
        } else {
            return Collections.emptyList();
        }
    }

    private Map<BugEntity, SuggestionEntity> suggestBugfixes(Collection<BugEntity> bugs) {
        Map<BugEntity, SuggestionEntity> bugfixes = new LinkedHashMap<>();
        OllamaAPI ollamaAPI = getOllamaAPIClient();
        for (BugEntity bug : bugs) {
            try {
                SuggestionEntity suggestion = suggestBugfix(ollamaAPI, bug);
                bugfixes.put(bug, suggestion);
            } catch (IOException | OllamaBaseException | InterruptedException | ToolInvocationException e) {
                getLog().error("Failed to suggest bugfix", e);
            }
        }
        return bugfixes;
    }

    private OllamaAPI getOllamaAPIClient() {
        OllamaAPI ollamaAPI = new OllamaAPI(ollamaHost);
        if (ollamaUsername != null && !ollamaUsername.isBlank() &&
                ollamaPassword != null && !ollamaPassword.isBlank()) {
            ollamaAPI.setBasicAuth(ollamaUsername, ollamaPassword);
        }
        ollamaAPI.setRequestTimeoutSeconds(modelRequestTimeout);
        return new OllamaAPI();
    }

    private SuggestionEntity suggestBugfix(OllamaAPI ollamaAPI, BugEntity bug)
            throws IOException, ToolInvocationException, OllamaBaseException, InterruptedException {
        String resultPrompt = replaceParametersInPrompt(prompt, bug);
        OllamaChatRequest request = OllamaChatRequestBuilder.getInstance(modelName)
                .withMessage(OllamaChatMessageRole.USER, resultPrompt)
                .build();
        OllamaChatResult result = ollamaAPI.chat(request);
        String responseText = result.getResponseModel().getMessage().getContent();
        return new SuggestionEntity(responseText);
    }

    private String replaceParametersInPrompt(String templatedPrompt, BugEntity bug) throws IOException {
        String bugContent = bug.getContent();
        String sourceFileContent = getSourceCode(bug.getSourceFilePath());

        return templatedPrompt
                .replace("%bugContent%", bugContent)
                .replace("%sourceFile%", sourceFileContent);
    }

    private String getSourceCode(String sourceFilePath) throws IOException {
        File sourceFile = sourceDirectory.toPath().resolve(sourceFilePath).toFile();
        return IOUtils.toString(sourceFile.toURI(), StandardCharsets.UTF_8);
    }

    private void dumpBugfixesToOutputFile() throws JAXBException {
        File outputFile = new File(sourceProjectBuildDirectory, suggestionsOutputFilename);
        getLog().info("Dumping " + bugfixes.size() + " bugfix suggestions to file " + outputFile);
        JAXBContext context = JAXBContext.newInstance(BugFixesDump.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(new BugFixesDump(bugfixes), outputFile);
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        try {
            generateSiteWithBugfixes(bugfixes);
        } catch (Exception e) {
            throw new MavenReportException("Error creating HTML report", e);
        }
    }

    private void generateSiteWithBugfixes(Map<BugEntity, SuggestionEntity> bugfixes)
            throws MavenReportException {
        getLog().info("Suggestions size: " + bugfixes.size());

        BugfixSuggesterReportGenerator reportGenerator = new BugfixSuggesterReportGenerator(
                getSink(),
                sourceProjectName,
                sourceProjectVersion,
                bugfixes
        );
        reportGenerator.generateReport();
    }

    @Override
    public String getOutputName() {
        return "bugfix-suggestions";
    }

    @Override
    public String getName(Locale locale) {
        return "Bugfix suggestions";
    }

    @Override
    public String getDescription(Locale locale) {
        return "Bugfix suggestions";
    }
}
