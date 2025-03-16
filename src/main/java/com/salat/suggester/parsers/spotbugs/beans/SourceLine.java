package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SourceLine {
    @XmlAttribute
    private String classname;

    @XmlAttribute
    private String end;

    @XmlAttribute
    private String endBytecode;

    @XmlAttribute
    private String primary;

    @XmlAttribute
    private String sourcefile;

    @XmlAttribute
    private String sourcepath;

    @XmlAttribute
    private String start;

    @XmlAttribute
    private String startBytecode;

    @XmlAttribute
    private String synthetic;

    @XmlElement
    private Message message;
}