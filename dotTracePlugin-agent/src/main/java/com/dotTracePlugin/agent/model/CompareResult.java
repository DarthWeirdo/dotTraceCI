package com.dotTracePlugin.agent.model;

/**
 * Created by Alexey.Totin on 5/18/2015.
 */
public class CompareResult {

    private String FQN;

    private int baseTotalTime;
    private int reportTotalTime;
    private int diffTotalTime;

    private int baseOwnTime;
    private int reportOwnTime;
    private int diffOwnTime;

    private boolean isSuccessful = true;

    public CompareResult(String fqn, int baseTotalTime, int reportTotalTime, int baseOwnTime,
                         int reportOwnTime){
        FQN = fqn;
        this.baseTotalTime = baseTotalTime;
        this.reportTotalTime = reportTotalTime;
        this.baseOwnTime = baseOwnTime;
        this.reportOwnTime = reportOwnTime;

        if (baseTotalTime != 0) {
            diffTotalTime = reportTotalTime - baseTotalTime;
            if (diffTotalTime > 0) isSuccessful = false;
        } else {
            diffTotalTime = 0;
        }

        if (baseOwnTime != 0) {
            diffOwnTime = reportOwnTime - baseOwnTime;
            if (diffOwnTime > 0) isSuccessful = false;
        } else {
            diffOwnTime = 0;
        }

    }

    public int getBaseTotalTime() {
        return baseTotalTime;
    }

    public int getReportTotalTime(){
        return reportTotalTime;
    }

    public int getBaseOwnTime(){
        return baseOwnTime;
    }

    public int getReportOwnTime(){
        return reportOwnTime;
    }

    public int getDiffTotalTime() {
        return diffTotalTime;
    }

    public int getDiffOwnTime() {
        return diffOwnTime;
    }

    public String getFQN() {
        return FQN;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
