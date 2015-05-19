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

    private int baseCalls;
    private int reportCalls;
    private int diffCalls;

    private int baseInstances;
    private int reportInstances;
    private int diffInstances;

    private boolean isSuccessful = true;

    public CompareResult(String fqn, int baseTotalTime, int reportTotalTime, int baseOwnTime,
                         int reportOwnTime, int baseCalls, int reportCalls, int baseInstances,
                         int reportInstances){
        FQN = fqn;
        this.baseTotalTime = baseTotalTime;
        this.reportTotalTime = reportTotalTime;
        this.baseOwnTime = baseOwnTime;
        this.reportOwnTime = reportOwnTime;
        this.baseCalls = baseCalls;
        this.reportCalls = reportCalls;
        this.baseInstances = baseInstances;
        this.reportInstances = reportInstances;

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

        if (baseCalls != 0) {
            diffCalls = reportCalls - baseCalls;
            if (diffCalls > 0) isSuccessful = false;
        } else {
            diffCalls = 0;
        }

        if (baseInstances != 0) {
            diffInstances = reportInstances - baseInstances;
            if (diffInstances > 0) isSuccessful = false;
        } else {
            diffInstances = 0;
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

    public int getBaseCalls(){
        return baseCalls;
    }

    public int getReportCalls(){
        return reportCalls;
    }

    public int getBaseInstances(){
        return baseInstances;
    }

    public int getReportInstances(){
        return reportInstances;
    }


    public int getDiffTotalTime() {
        return diffTotalTime;
    }

    public int getDiffOwnTime() {
        return diffOwnTime;
    }

    public int getDiffCalls() {
        return diffCalls;
    }

    public int getDiffInstances() {
        return diffInstances;
    }

    public String getFQN() {
        return FQN;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
