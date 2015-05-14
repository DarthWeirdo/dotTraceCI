package com.dotTracePlugin.agent.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Alexey.Totin on 5/13/2015.
 */
@XmlRootElement(name = "Patterns")
public class ReportPatterns {

    private List<ReportPattern> patterns;

    public List<ReportPattern> getPatterns() {
        return patterns;
    }

    @XmlElement(name = "Pattern")
    public void setPatterns(List<ReportPattern> patterns) {
        this.patterns = patterns;
    }
}
