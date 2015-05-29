package com.dotTracePlugin.agent.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class ProfiledMethod {

    @XmlAttribute(name = "FQN")
    private String FQN;

    @XmlAttribute(name = "TotalTime")
    private String TotalTime;

    @XmlAttribute(name = "OwnTime")
    private String OwnTime;


    public ProfiledMethod(){

    }

    public ProfiledMethod(String fQN, String totalTime, String ownTime){
        FQN = fQN;
        TotalTime = totalTime;
        OwnTime = ownTime;
    }

    public String getFQN() {
        return FQN;
    }

    public void setFQN(String FQN) {
        this.FQN = FQN;
    }

    public String getTotalTime() {
        return TotalTime;
    }


    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getOwnTime() {
        return OwnTime;
    }


    public void setOwnTime(String ownTime) {
        OwnTime = ownTime;
    }


}
