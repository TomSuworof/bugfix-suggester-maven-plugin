package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Class {
    @XmlAttribute
    private String classAnnotationNames;

    @XmlAttribute
    private String classname;

    @XmlAttribute
    private String primary;

    @XmlElement(name = "Message")
    private Message message;

    @XmlElement(name = "SourceLine")
    private SourceLine sourceLine;
}