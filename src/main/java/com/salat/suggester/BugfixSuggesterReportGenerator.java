package com.salat.suggester;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.reporting.MavenReportException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Map;

public class BugfixSuggesterReportGenerator {
    private final Sink sink;
    private final String sourceProjectName;
    private final String sourceProjectVersion;
    private final Map<BugEntity, SuggestionEntity> bugfixes;

    public BugfixSuggesterReportGenerator(
            Sink sink,
            String sourceProjectName,
            String sourceProjectVersion,
            Map<BugEntity, SuggestionEntity> bugfixes
    ) {
        this.sink = sink;
        this.sourceProjectName = sourceProjectName;
        this.sourceProjectVersion = sourceProjectVersion;
        this.bugfixes = bugfixes;
    }

    public void generateReport() throws MavenReportException {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        if (sink == null) {
            throw new MavenReportException("Could not get the Doxia sink");
        }

        sink.head();
        sink.title();
        sink.text("Bugfix suggestions report for " + sourceProjectName + " " + sourceProjectVersion);
        sink.title_();
        sink.head_();

        sink.body();

        for (Map.Entry<BugEntity, SuggestionEntity> bugfix : bugfixes.entrySet()) {
            BugEntity bug = bugfix.getKey();
            SuggestionEntity suggestion = bugfix.getValue();

            {
                sink.section1();
                {
                    sink.sectionTitle1();
                    sink.text(bug.getTitle());
                    sink.sectionTitle1_();
                }
                {
                    sink.blockquote();
                    sink.text(bug.getContent());
                    sink.blockquote_();
                }
                {
                    String suggestionContent = suggestion.getContent();

                    sink.paragraph();
                    sink.rawText(renderer.render(parser.parse(suggestionContent)));
                    sink.paragraph_();
                }
                sink.section1_();
            }
        }

        sink.body_();
    }
}
