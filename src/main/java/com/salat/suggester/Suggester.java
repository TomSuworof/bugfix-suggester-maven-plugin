package com.salat.suggester;

public interface Suggester {
    SuggestionEntity suggestFix(BugEntity bug);
}
