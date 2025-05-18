<img src="./docs/assets/logo.png" width="100px" align="left">

### `bugfix-suggester-maven-plugin`

<br><br>

Maven plugin, that accepts files with bug summaries and provides suggestions how to fix them.

Currently only [SpotBugs](https://github.com/spotbugs/spotbugs-maven-plugin) files are supported.

Suggestions are given by AI models from [ollama](https://github.com/ollama/ollama) server.

## How to use

1. Add plugin to `pom.xml` reporting section after analyzer plugin (i.e. SpotBugs).
2. Run Maven `site` goal: `mvn site`.
3. Wait while AI doing its job...
4. Find bugs and suggestions for them in `target/site/bugfix-suggestions.html`.

Example in `pom.xml`:
```xml
<reporting>
        <!-- mvn com.salat:bugfix-suggester-maven-plugin:suggest -->
        <plugin>
            <groupId>com.salat.bugfix-suggester</groupId>
            <artifactId>bugfix-suggester-maven-plugin</artifactId>
            <version>0.0.3</version>
            <configuration>
                <modelName>qwen2.5</modelName>
                <prompt>SpotBugs after analysis gives this error. Suggest a fix. The error: %bugContent%. Source code: ```%sourceFile%```. Keep the answer small and precise, code mostly.</prompt>
            </configuration>
        </plugin>
    </plugins>
</reporting>
```