package com.salat.suggester.parsers.spotbugs.beans;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BugInstance {
    @XmlAttribute
    private String abbrev;

    @XmlAttribute
    private String category;

    @XmlAttribute
    private String cweid;

    @XmlAttribute
    private String instanceHash;

    @XmlAttribute
    private String instanceOccurrenceMax;

    @XmlAttribute
    private String instanceOccurrenceNum;

    @XmlAttribute
    private String priority;

    @XmlAttribute
    private String rank;

    @XmlAttribute(name = "type")
    private String typeAttribute;

    @XmlElement(name = "Class")
    private Class aClass;

    @XmlElement(name = "Field")
    private Field field;

    @XmlElement(name = "LocalVariable")
    private LocalVariable localVariable;

    @XmlElement(name = "LongMessage")
    private LongMessage longMessage;

    @XmlElement(name = "Method")
    private Method method;

    @XmlElement(name = "ShortMessage")
    private ShortMessage shortMessage;

    @XmlElement(name = "SourceLine")
    private SourceLine sourceLine;

    @XmlElement(name = "Type")
    private Type type;
}