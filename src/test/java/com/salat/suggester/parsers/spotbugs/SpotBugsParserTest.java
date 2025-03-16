package com.salat.suggester.parsers.spotbugs;

import com.salat.suggester.BugEntity;
import com.salat.suggester.parsers.Parser;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class SpotBugsParserTest {
    @Test
    public void test() {
        File spotbugsFile = new File("src/test/resources/spotbugsXml.xml"); // put your file to resources folder to run test
        Parser parser = new SpotBugsParser();
        List<BugEntity> bugs = parser.parse(spotbugsFile);
        assertNotEquals(0, bugs.size());
    }
}