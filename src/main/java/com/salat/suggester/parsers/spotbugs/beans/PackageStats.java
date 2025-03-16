package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageStats {
    @XmlAttribute
    private String packageName;

    @XmlAttribute
    private String priority_1;

    @XmlAttribute
    private String priority_2;

    @XmlAttribute
    private String total_bugs;

    @XmlAttribute
    private String total_size;

    @XmlAttribute
    private String total_types;

    @XmlElement(name = "ClassStats")
    private ClassStats classStats;
}