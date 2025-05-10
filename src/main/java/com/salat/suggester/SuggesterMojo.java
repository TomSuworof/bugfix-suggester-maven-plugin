package com.salat.suggester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salat.suggester.parsers.spotbugs.SpotBugsParser;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import org.apache.commons.io.IOUtils;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Mojo(name = "suggest")
public class SuggesterMojo extends AbstractMavenReport {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Location of file, that contains bugs messages from code analysers, for example, spotbugsXml.xml.
     */
    @Parameter(property = "inputFileWithBugs", required = true)
    private File inputFileWithBugs;

    /**
     * Model from Ollama to use for generation suggestions.
     */
    @Parameter(property = "modelName", readonly = true)
    private String modelName;

    /**
     * Prompt for specified model.
     * <br><br>
     * Supports arguments placement:
     * <ul>
     *     <li><pre>%bugContent%</pre> will be replaced by actual bug description</li>
     * </ul>
     */
    @Parameter(property = "prompt", readonly = true, required = true)
    private String prompt;

    /**
     * Request timeout to model in seconds. Default is 600 seconds.
     */
    @Parameter(property = "modelRequestTimeout", defaultValue = "600", readonly = true)
    private long modelRequestTimeout;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDirectory;

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        List<BugEntity> bugs;
        {
            try {
                bugs = parseFileAndCollectBugs();
            } catch (Exception e) {
                throw new MavenReportException("Error parsing bugs from file", e);
            }
        }
        bugs.stream().map(BugEntity::content).forEach(
                s -> getLog().info("Bug: " + s)
        );

        List<SuggestionEntity> suggestions;
        {
            try {
                suggestions = suggestFixesForBugs(bugs);
            } catch (Exception e) {
                throw new MavenReportException("Error making API call to ollama", e);
            }
        }
        suggestions.stream().map(SuggestionEntity::content).forEach(
                s -> getLog().info("Suggestion: " + s)
        );

        {
            try {
                generateSiteWithBugsAndSuggestions(bugs, suggestions);
            } catch (Exception e) {
                throw new MavenReportException("Error creating HTML report", e);
            }
        }
    }

    private List<BugEntity> parseFileAndCollectBugs() {
        return new SpotBugsParser().parse(inputFileWithBugs);
    }

    private List<SuggestionEntity> suggestFixesForBugs(Collection<BugEntity> bugs)
            throws OllamaBaseException, IOException, InterruptedException {
        List<SuggestionEntity> suggestions = new LinkedList<>();

        OllamaAPI ollamaAPI = new OllamaAPI();
        ollamaAPI.setRequestTimeoutSeconds(modelRequestTimeout);
        for (BugEntity bug : bugs) {
            String bugContent = bug.content();
            String sourceFileContent = getSourceCode(bug.sourceFilePath());

            String resultPrompt = prompt
                    .replace("%bugContent%", bugContent)
                    .replace("%sourceFile%", sourceFileContent);

            OllamaChatRequest request = OllamaChatRequestBuilder.getInstance(modelName)
                    .withMessage(OllamaChatMessageRole.USER, resultPrompt)
                    .build();
            OllamaChatResult result = ollamaAPI.chat(request);
            String responseText = objectMapper.reader().readTree(result.toString()).get("response").toString();
            suggestions.add(new SuggestionEntity(responseText));
        }
        return suggestions;
    }

    private String getSourceCode(String sourceFilePath) throws IOException {
        File sourceFile = sourceDirectory.toPath().resolve(sourceFilePath).toFile();
        return IOUtils.toString(sourceFile.toURI(), StandardCharsets.UTF_8);
    }

    private void generateSiteWithBugsAndSuggestions(List<BugEntity> bugs, List<SuggestionEntity> suggestions)
            throws MavenReportException {
        getLog().info("Bug list size: " + bugs.size());
        getLog().info("Suggestion list size: " + suggestions.size());

        if (bugs.size() != suggestions.size()) {
            throw new IllegalStateException(String.format(
                    "Bugs list size is not equal to suggestion list size. Bugs: %d, Suggestions: %d",
                    bugs.size(),
                    suggestions.size()
            ));
        }

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        Sink mainSink = getSink();
        if (mainSink == null) {
            throw new MavenReportException("Could not get the Doxia sink");
        }

        mainSink.head();
        mainSink.title();
        mainSink.text("Bugfix suggestions report for " + project.getName() + " " + project.getVersion());
        mainSink.title_();
        mainSink.head_();

        mainSink.body();

        for (int i = 0; i < bugs.size(); i++) {
            BugEntity bug = bugs.get(i);
            SuggestionEntity suggestion = suggestions.get(i);

            getLog().info("Bug: " + bug.content());
            getLog().info("Suggestion: " + suggestion.content());

            {
                mainSink.section1();
                {
                    mainSink.sectionTitle1();
                    mainSink.text("Bug #" + (i + 1));
                    mainSink.sectionTitle1_();
                }
                {
                    mainSink.paragraph();
                    mainSink.text(bug.content());
                    mainSink.paragraph_();
                }
                {
                    String suggestionContent = suggestion.content()
                            .replace("\\n", "\n");

                    mainSink.paragraph();
                    mainSink.rawText(renderer.render(parser.parse(suggestionContent)));
                    mainSink.paragraph_();
                }
                mainSink.section1_();
            }
        }

        mainSink.body_();
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
