package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
    @XmlAttribute
    private String projectName;

    @XmlElement(name = "AuxClasspathEntry")
    private AuxClasspathEntry auxClasspathEntry;

    @XmlElement(name = "Jar")
    private Jar jar;

    @XmlElement(name = "SrcDir")
    private SrcDir srcDir;

    @XmlElement(name = "WrkDir")
    private WrkDir wrkDir;
}