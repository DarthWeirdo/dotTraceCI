package com.dotTracePlugin.agent.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class ProfiledMethod {

    @XmlAttribute(name = "Id")
    private String Id;

    @XmlAttribute(name = "FQN")
    private String FQN;

    @XmlAttribute(name = "TotalTime")
    private String TotalTime;

    @XmlAttribute(name = "OwnTime")
    private String OwnTime;

    @XmlAttribute(name = "Calls")
    private String Calls;

    @XmlAttribute(name = "Instances")
    private String Instances;


    public ProfiledMethod(){

    }

    public ProfiledMethod(String id, String fQN, String totalTime, String ownTime, String calls, String instances){
        Id = id;
        FQN = fQN;
        TotalTime = totalTime;
        OwnTime = ownTime;
        Calls = calls;
        Instances = instances;
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

    public String getCalls() {
        return Calls;
    }


    public void setCalls(String calls) {
        Calls = calls;
    }

    public String getInstances() {
        return Instances;
    }


    public void setInstances(String instances) {
        Instances = instances;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
