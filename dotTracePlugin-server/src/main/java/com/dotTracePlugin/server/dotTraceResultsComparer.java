package com.dotTracePlugin.server;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import com.dotTracePlugin.server.model.ProfilingResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey.Totin on 6/5/2015.
 */
public class dotTraceResultsComparer {
    private Map<String, ProfilingResult> myResult = new HashMap<String, ProfilingResult>();
    private boolean isSuccessful = true;


    public dotTraceResultsComparer(Map<String, Integer> myBaseTotalTime, Map<String, Integer> myBaseOwnTime,
                            Map<String, Integer> myReportTotalTime, Map<String, Integer> myReportOwnTime) {

        for (Map.Entry<String,Integer> entry : myBaseTotalTime.entrySet()) {
            String currentKey = entry.getKey();
            Integer currentBaseTotalTime = myBaseTotalTime.get(currentKey);
            Integer currentReportTotalTime = myReportTotalTime.get(currentKey);
            Integer currentBaseOwnTime = myBaseOwnTime.get(currentKey);
            Integer currentReportOwnTime = myReportOwnTime.get(currentKey);

            ProfilingResult currentResult = new ProfilingResult(currentKey, currentBaseTotalTime, currentReportTotalTime,
                    currentBaseOwnTime,currentReportOwnTime);
            if (!currentResult.isSuccessful()){
                this.isSuccessful = false;
            }
            myResult.put(currentKey, currentResult);
        }
    }

    @NotNull
    public Map <String, ProfilingResult> getComparisonAsMap(){
        return myResult;
    }

    @NotNull
    public String getComparisonAsString(){
        StringBuilder result = new StringBuilder();
        String line;
        result.append("Profiling results: \n");

        for (Map.Entry<String,ProfilingResult> entry : myResult.entrySet()) {
            if (entry.getValue().isSuccessful())
                result.append("PASSED: ");
            else result.append("FAILED: ");

            result.append(entry.getValue().getFQN());
            result.append("\n\t");

            if (entry.getValue().getBaseTotalTime() != -1){
                int baseTotalTime = entry.getValue().getBaseTotalTime();
                if (baseTotalTime == -2) {baseTotalTime = 0;}
                line = String.format("Total time, ms [expected %d | measured %d | delta %d]",
                        baseTotalTime, entry.getValue().getReportTotalTime(),
                        entry.getValue().getDiffTotalTime());
                result.append(line);
                result.append("\n\t");
            }

            if (entry.getValue().getBaseOwnTime() != -1){
                int baseOwnTime = entry.getValue().getBaseOwnTime();
                if (baseOwnTime == -2) {baseOwnTime = 0;}
                line = String.format("Own time, ms [expected %d | measured %d | delta %d]",
                        baseOwnTime, entry.getValue().getReportOwnTime(),
                        entry.getValue().getDiffOwnTime());
                result.append(line);
                result.append("\n\t");
            }
            result.append("\n");
        }

        return result.toString();
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
