package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.ProfilingResult;
import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.common.dotTraceRunnerConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/18/2015.
 */
public class dotTraceValuePublisher {
    private Map<String, ProfilingResult> myDiff = new HashMap<String, ProfilingResult>();


    public dotTraceValuePublisher(Map<String, ProfiledMethod> baseValues, Map<String, ProfiledMethod> reportValues) {
        for (Map.Entry<String,ProfiledMethod> entry : baseValues.entrySet()) {
            String currentKey = entry.getKey();
            if (reportValues.containsKey(currentKey)){
                ProfiledMethod currentBaseMethod = baseValues.get(currentKey);
                ProfiledMethod currentReportMethod = reportValues.get(currentKey);
                String baseTotalTime = (currentBaseMethod.getTotalTime());
                String reportTotalTime = (currentReportMethod.getTotalTime());
                String baseOwnTime = (currentBaseMethod.getOwnTime());
                String reportOwnTime = (currentReportMethod.getOwnTime());

                ProfilingResult currentResult = new ProfilingResult(currentKey, baseTotalTime, reportTotalTime,
                        baseOwnTime, reportOwnTime);
                myDiff.put(currentKey, currentResult);
            }
        }
    }

    @NotNull
    public Map <String, ProfilingResult> getValuesAsMap(){
        return myDiff;
    }

    @NotNull
    public String getValuesAsServiceMessage(){
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String,ProfilingResult> entry : myDiff.entrySet()) {

                result.append(String.format(
                        "##teamcity[%s key='%s:baseTotalTime' value='%s'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getBaseTotalTime()));

                result.append(String.format(
                        "##teamcity[%s key='%s:reportTotalTime' value='%s'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getReportTotalTime()));

                result.append(String.format(
                        "##teamcity[%s key='%s:baseOwnTime' value='%s'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getBaseOwnTime()));

                result.append(String.format(
                        "##teamcity[%s key='%s:reportOwnTime' value='%s'] \n",
                        dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
                        entry.getValue().getFQN(), entry.getValue().getReportOwnTime()));
        }

        return result.toString();
    }

}
