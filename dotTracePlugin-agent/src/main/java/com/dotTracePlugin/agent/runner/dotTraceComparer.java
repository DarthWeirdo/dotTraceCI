package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.CompareResult;
import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.common.dotTraceRunnerConstants;
import org.jetbrains.annotations.NotNull;

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
        for (Map.Entry<String,ProfiledMethod> entry : myBaseValues.entrySet()) {
            String currentKey = entry.getKey();
            if (myReportValues.containsKey(currentKey)){
                ProfiledMethod currentBaseMethod = myBaseValues.get(currentKey);
                ProfiledMethod currentReportMethod = myReportValues.get(currentKey);

                int baseTotalTime = Integer.parseInt(currentBaseMethod.getTotalTime());
                int reportTotalTime = Integer.parseInt(currentReportMethod.getTotalTime());
                int baseOwnTime = Integer.parseInt(currentBaseMethod.getOwnTime());
                int reportOwnTime = Integer.parseInt(currentReportMethod.getOwnTime());

                CompareResult currentResult = new CompareResult(currentKey, baseTotalTime, reportTotalTime,
                        baseOwnTime, reportOwnTime);
                this.isSuccessful = currentResult.isSuccessful();
                myDiff.put(currentKey, currentResult);
            }
        }
    }

    @NotNull
    public Map <String, CompareResult> getComparisonAsMap(){
        return myDiff;
    }

    @NotNull
    public String getComparisonAsString(){
        StringBuilder result = new StringBuilder();
        String line;
        result.append("Profiling results: \n");

        for (Map.Entry<String,CompareResult> entry : myDiff.entrySet()) {
            if (entry.getValue().isSuccessful())
                result.append("PASSED: ");
            else result.append("FAILED: ");

            result.append(entry.getValue().getFQN());
            result.append("\n\t");

            if (entry.getValue().getBaseTotalTime() != 0){
                line = String.format("Total time, ms [expected %d | measured %d | delta %d]",
                        entry.getValue().getBaseTotalTime(), entry.getValue().getReportTotalTime(),
                        entry.getValue().getDiffTotalTime());
                result.append(line);
                result.append("\n\t");
            }

            if (entry.getValue().getBaseOwnTime() != 0){
                line = String.format("Own time, ms [expected %d | measured %d | delta %d]",
                        entry.getValue().getBaseOwnTime(), entry.getValue().getReportOwnTime(),
                        entry.getValue().getDiffOwnTime());
                result.append(line);
                result.append("\n\t");
            }
            result.append("\n");
        }

        return result.toString();
    }

    @NotNull
    public String getComparisonAsServiceMessage(){
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String,CompareResult> entry : myDiff.entrySet()) {

            if (entry.getValue().getBaseTotalTime() != 0){
                result.append(String.format(
                        "##teamcity[%s key='%s:baseTotalTime' value='%d'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getBaseTotalTime()));

                result.append(String.format(
                        "##teamcity[%s key='%s:reportTotalTime' value='%d'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getReportTotalTime()));
            }

            if (entry.getValue().getBaseOwnTime() != 0){
                result.append(String.format(
                        "##teamcity[%s key='%s:baseOwnTime' value='%d'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getBaseOwnTime()));

                result.append(String.format(
                        "##teamcity[%s key='%s:reportOwnTime' value='%d'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getReportOwnTime()));
            }
        }

        return result.toString();
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
