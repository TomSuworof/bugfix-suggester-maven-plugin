package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BugCategory {
    @XmlAttribute
    private String category;

    @XmlElement(name = "Description")
    private Description description;
}