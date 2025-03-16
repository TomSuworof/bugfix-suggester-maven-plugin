package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassStats {
    @XmlAttribute
    private String bugs;

    @XmlAttribute
    private String className;

    @XmlAttribute
    private String interfaceName;

    @XmlAttribute
    private String priority_1;

    @XmlAttribute
    private String priority_2;

    @XmlAttribute
    private String size;

    @XmlAttribute
    private String sourceFile;
}