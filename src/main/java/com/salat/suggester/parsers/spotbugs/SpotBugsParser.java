package com.salat.suggester.parsers.spotbugs;

import com.salat.suggester.BugEntity;
import com.salat.suggester.parsers.Parser;
import com.salat.suggester.parsers.spotbugs.beans.BugCollection;
import com.salat.suggester.parsers.spotbugs.beans.BugInstance;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SpotBugsParser implements Parser {
    @Override
    public List<BugEntity> parse(File bugsReportFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(BugCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<BugCollection> root = unmarshaller.unmarshal(new StreamSource(bugsReportFile), BugCollection.class);
            BugCollection bugCollection = root.getValue();

            List<BugEntity> bugs = new LinkedList<>();

            assert bugCollection.getBugInstanceList() != null;

            for (BugInstance bugInstance : bugCollection.getBugInstanceList()) {
                BugEntity bug = new BugEntity(
                        bugInstance.getLongMessage().getTextContent(),
                        bugInstance.getAClass().getSourceLine().getSourcepath()
                );
                bugs.add(bug);
            }
            return bugs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SpotBugs report", e);
        }
    }
}
