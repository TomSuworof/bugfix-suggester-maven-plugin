package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BugCollection {
    @XmlAttribute
    private String analysisTimestamp;

    @XmlAttribute
    private String release;

    @XmlAttribute
    private String sequence;

    @XmlAttribute
    private String timestamp;

    @XmlAttribute
    private String version;

    @XmlElement(name = "BugCategory")
    private BugCategory bugCategory;

    @XmlElements({@XmlElement(name = "BugCode")})
    private List<BugCode> bugCodeList;

    @XmlElements({@XmlElement(name = "BugInstance")})
    private List<BugInstance> bugInstanceList;

    @XmlElements({@XmlElement(name = "BugPattern")})
    private List<BugPattern> bugPatternList;

    @XmlElement(name = "ClassFeatures")
    private ClassFeatures classFeatures;

    @XmlElement(name = "Errors")
    private Errors errors;

    @XmlElement(name = "FindBugsSummary")
    private FindBugsSummary findBugsSummary;

    @XmlElement(name = "History")
    private History history;

    @XmlElement(name = "Project")
    private Project project;
}