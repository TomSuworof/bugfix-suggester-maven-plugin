package com.salat.suggester;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class BugFixesDump {
    @XmlElement(name = "bugFix")
    private List<BugFix> bugFixes;

    public BugFixesDump(Map<BugEntity, SuggestionEntity> bugfixesMap) {
        bugFixes = bugfixesMap.entrySet().stream().map(e -> new BugFix(e.getKey(), e.getValue())).toList();
    }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class BugFix {
    @XmlElement
    private BugEntity bug;
    @XmlElement
    private SuggestionEntity suggestion;
}
