package com.salat.suggester;

public record BugEntity(
        String title,
        String content,
        String sourceFilePath
) {
}
