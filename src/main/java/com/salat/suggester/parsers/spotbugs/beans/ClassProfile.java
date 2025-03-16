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
public class ClassProfile {
    @XmlAttribute
    private String avgMicrosecondsPerInvocation;

    @XmlAttribute
    private String invocations;

    @XmlAttribute
    private String maxMicrosecondsPerInvocation;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String standardDeviationMicrosecondsPerInvocation;

    @XmlAttribute
    private String totalMilliseconds;
}