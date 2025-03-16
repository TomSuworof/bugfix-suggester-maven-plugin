package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalVariable {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String pc;

    @XmlAttribute
    private String register;

    @XmlAttribute
    private String role;

    @XmlElement(name = "Message")
    private Message message;
}