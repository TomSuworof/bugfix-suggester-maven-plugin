# bugfix-suggester-maven-plugin

Maven plugin, that accepts files with bug summaries and provides suggestions how to fix them.

Currently only [SpotBugs](https://github.com/spotbugs/spotbugs-maven-plugin) files are supported.

Suggestions are given by AI models from [ollama](https://github.com/ollama/ollama) server.

## How to use

1. Build plugin locally (yeah, I haven't deployed it yet).
2. Add plugin to `pom.xml` reporting section after analyzer plugin (i.e. SpotBugs).
3. Run Maven `site` goal: `mvn site`.
4. Wait while AI doing its job...
5. Find bugs and suggestions for them in `target/site/bugfix-suggestions.html`.

Example in `pom.xml`:
```xml
<reporting>
        <!-- mvn com.salat:bugfix-suggester-maven-plugin:suggest -->
        <plugin>
            <groupId>com.salat.bugfix-suggester</groupId>
            <artifactId>bugfix-suggester-maven-plugin</artifactId>
            <version>0.0.1</version>
            <configuration>
                <inputFileWithBugs>${build.directory}/spotbugsXml.xml</inputFileWithBugs>
                <modelName>qwen2.5</modelName>
                <prompt>SpotBugs after analysis gives this error. Suggest a fix. The error: %bugContent%. Keep the answer small and precise, code mostly.</prompt>
            </configuration>
        </plugin>
    </plugins>
</reporting>
```