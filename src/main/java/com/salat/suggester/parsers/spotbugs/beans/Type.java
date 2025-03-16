package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Type {
    @XmlAttribute
    private String descriptor;

    @XmlAttribute
    private String role;

    @XmlElement
    private Message message;

    @XmlElement
    private SourceLine sourceLine;
}