package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BugPattern {
    @XmlAttribute
    private String abbrev;

    @XmlAttribute
    private String category;

    @XmlAttribute
    private String cweid;

    @XmlAttribute
    private String type;

    @XmlElement(name = "Details")
    private Details details;

    @XmlElement(name = "ShortDescription")
    private ShortDescription shortDescription;
}