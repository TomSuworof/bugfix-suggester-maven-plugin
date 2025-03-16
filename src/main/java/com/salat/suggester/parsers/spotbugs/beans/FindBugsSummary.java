package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FindBugsSummary {
    @XmlAttribute
    private String alloc_mbytes;

    @XmlAttribute
    private String clock_seconds;

    @XmlAttribute
    private String cpu_seconds;

    @XmlAttribute
    private String gc_seconds;

    @XmlAttribute
    private String java_version;

    @XmlAttribute
    private String num_packages;

    @XmlAttribute
    private String peak_mbytes;

    @XmlAttribute
    private String priority_1;

    @XmlAttribute
    private String priority_2;

    @XmlAttribute
    private String referenced_classes;

    @XmlAttribute
    private String timestamp;

    @XmlAttribute
    private String total_bugs;

    @XmlAttribute
    private String total_classes;

    @XmlAttribute
    private String total_size;

    @XmlAttribute
    private String vm_version;

    @XmlElement(name = "FileStats")
    private FileStats fileStats;

    @XmlElement(name = "FindBugsProfile")
    private FindBugsProfile findBugsProfile;

    @XmlElement(name = "PackageStats")
    private PackageStats packageStats;
}