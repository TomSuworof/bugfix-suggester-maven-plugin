package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BugCode {
    @XmlAttribute
    private String abbrev;

    @XmlElement(name = "Description")
    private Description description;
}