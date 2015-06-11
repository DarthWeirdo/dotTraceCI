package com.dotTracePlugin.agent.model;

/**
 * Created by Alexey.Totin on 5/18/2015.
 */
public class ProfilingResult {

    private String FQN;
    private String baseTotalTime;
    private String reportTotalTime;
    private String baseOwnTime;
    private String reportOwnTime;

    public ProfilingResult(String fqn, String baseTotalTime, String reportTotalTime, String baseOwnTime,
                           String reportOwnTime){
        FQN = fqn;
        this.baseTotalTime = baseTotalTime;
        this.reportTotalTime = reportTotalTime;
        this.baseOwnTime = baseOwnTime;
        this.reportOwnTime = reportOwnTime;
    }

    public String getBaseTotalTime() {
        return baseTotalTime;
    }

    public String getReportTotalTime(){
        return reportTotalTime;
    }

    public String getBaseOwnTime(){
        return baseOwnTime;
    }

    public String getReportOwnTime(){
        return reportOwnTime;
    }

    public String getFQN() {
        return FQN;
    }


}
