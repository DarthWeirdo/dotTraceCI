package com.dotTracePlugin.agent.model;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */
public class ProfiledMethod {
    public String FQN;
    public String TotalTime;
    public String OwnTime;
    public String Calls;
    public String Instances;

    public ProfiledMethod(String fQN, String totalTime, String ownTime, String calls, String instances){
        FQN = fQN;
        TotalTime = totalTime;
        OwnTime = ownTime;
        Calls = calls;
        Instances = instances;
    }
}
