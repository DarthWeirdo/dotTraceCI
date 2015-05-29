package com.dotTracePlugin.agent.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */

@XmlRootElement(name = "Pattern")
public class ReportPattern {
    private String Pattern;



    public String getPattern() {
        return Pattern;
    }

    @XmlValue()
    public void setPattern(String pattern) {
        Pattern = pattern;
    }
}
