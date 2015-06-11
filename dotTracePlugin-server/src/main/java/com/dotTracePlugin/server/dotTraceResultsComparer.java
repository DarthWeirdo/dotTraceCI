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
    private Map<String, Integer> myBaseTotalTime;
    private Map<String, Integer> myBaseOwnTime;
    private Map<String, Integer> myReportTotalTime;
    private Map<String, Integer> myReportOwnTime;
    private Map<String, ProfilingResult> myResult = new HashMap<String, ProfilingResult>();

    private boolean isSuccessful = true;


    public dotTraceResultsComparer(Map<String, Integer> myBaseTotalTime, Map<String, Integer> myBaseOwnTime,
                            Map<String, Integer> myReportTotalTime, Map<String, Integer> myReportOwnTime) {
        this.myBaseTotalTime = myBaseTotalTime;
        this.myBaseOwnTime = myBaseOwnTime;
        this.myReportTotalTime = myReportTotalTime;
        this.myReportOwnTime = myReportOwnTime;

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
                line = String.format("Total time, ms [expected %d | measured %d | delta %d]",
                        entry.getValue().getBaseTotalTime(), entry.getValue().getReportTotalTime(),
                        entry.getValue().getDiffTotalTime());
                result.append(line);
                result.append("\n\t");
            }

            if (entry.getValue().getBaseOwnTime() != -1){
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

//    @NotNull
//    public String getComparisonAsServiceMessage(){
//        StringBuilder result = new StringBuilder();
//
//        for (Map.Entry<String,CompareResult> entry : myDiff.entrySet()) {
//
////            if (entry.getValue().getBaseTotalTime() != "0"){
//            result.append(String.format(
//                    "##teamcity[%s key='%s:baseTotalTime' value='%s'] \n",
//                    dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
//                    entry.getValue().getFQN(), entry.getValue().getBaseTotalTime()));
//
//            result.append(String.format(
//                    "##teamcity[%s key='%s:reportTotalTime' value='%s'] \n",
//                    dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
//                    entry.getValue().getFQN(), entry.getValue().getReportTotalTime()));
////            }
//
////            if (entry.getValue().getBaseOwnTime() != "0"){
//            result.append(String.format(
//                    "##teamcity[%s key='%s:baseOwnTime' value='%s'] \n",
//                    dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
//                    entry.getValue().getFQN(), entry.getValue().getBaseOwnTime()));
//
//            result.append(String.format(
//                    "##teamcity[%s key='%s:reportOwnTime' value='%s'] \n",
//                    dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME,
//                    entry.getValue().getFQN(), entry.getValue().getReportOwnTime()));
////            }
//        }
//
//        return result.toString();
//    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
