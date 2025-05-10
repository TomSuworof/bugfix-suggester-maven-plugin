package com.salat.suggester.parsers.spotbugs;

import com.salat.suggester.BugEntity;
import com.salat.suggester.parsers.Parser;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertNotEquals;

public class SpotBugsParserTest {
    @Test
    public void test() throws URISyntaxException {
        File spotbugsFile = new File(Objects.requireNonNull(SpotBugsParserTest.class.getResource("/spotbugsXml.xml")).toURI());
        Parser parser = new SpotBugsParser();
        List<BugEntity> bugs = parser.parse(spotbugsFile);
        assertNotEquals(0, bugs.size());
    }
}