package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.CompareResult;
import com.dotTracePlugin.agent.model.ProfiledMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/18/2015.
 */
public class dotTraceComparer {
    private Map<String, ProfiledMethod> myBaseValues;
    private Map<String, ProfiledMethod> myReportValues;
    private Map<String, CompareResult> myDiff = new HashMap<String, CompareResult>();

    private boolean isSuccessful = true;

    public dotTraceComparer(Map<String, ProfiledMethod> baseValues, Map<String, ProfiledMethod> reportValues) {
        this.myBaseValues = baseValues;
        this.myReportValues = reportValues;
        Compare();
    }

    private void Compare(){
        for (Map.Entry<String,ProfiledMethod> entry : myBaseValues.entrySet()) {
            String currentKey = entry.getKey();
            if (myReportValues.containsKey(currentKey)){
                ProfiledMethod currentBaseMethod = myBaseValues.get(currentKey);
                ProfiledMethod currentReportMethod = myReportValues.get(currentKey);

                int baseTotalTime = Integer.parseInt(currentBaseMethod.getTotalTime());
                int reportTotalTime = Integer.parseInt(currentReportMethod.getTotalTime());
                int baseOwnTime = Integer.parseInt(currentBaseMethod.getOwnTime());
                int reportOwnTime = Integer.parseInt(currentReportMethod.getOwnTime());
                int baseCalls = Integer.parseInt(currentBaseMethod.getCalls());
                int reportCalls = Integer.parseInt(currentReportMethod.getCalls());
                int baseInstances = Integer.parseInt(currentBaseMethod.getInstances());
                int reportInstances = Integer.parseInt(currentReportMethod.getInstances());

                CompareResult currentResult = new CompareResult(currentKey, baseTotalTime, reportTotalTime, baseOwnTime, reportOwnTime, baseCalls, reportCalls, baseInstances, reportInstances);
                if (currentResult.isSuccessful()) this.isSuccessful = false;
                myDiff.put(currentKey, currentResult);
            }
        }

    }

    public Map <String, CompareResult> getComparisonAsMap(){
        return myDiff;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
