package com.dotTracePlugin.server;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import com.dotTracePlugin.server.model.ProfilingResult;
import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.log.LogUtil;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageTranslator;
import jetbrains.buildServer.serverSide.BuildHistory;
import jetbrains.buildServer.serverSide.SFinishedBuild;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.ServerExtensionHolder;
import jetbrains.buildServer.serverSide.statistics.build.BuildDataStorage;
import jetbrains.buildServer.util.Hash;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Alexey.Totin on 5/20/2015.
 */
public class dotTraceStatisticValueTranslator implements ServiceMessageTranslator {

    private final BuildDataStorage myStorage;
    private Map<String, Integer> myPerfBaseOwnTime = new HashMap<String, Integer>();
    private Map<String, Integer> myPerfBaseTotalTime = new HashMap<String, Integer>();
    private Map<String, Integer> myPerfReportOwnTime = new HashMap<String, Integer>();
    private Map<String, Integer> myPerfReportTotalTime = new HashMap<String, Integer>();

    public dotTraceStatisticValueTranslator(ServerExtensionHolder server, BuildDataStorage storage) {
        myStorage = storage;
        server.registerExtension(ServiceMessageTranslator.class, getClass().getName(), this);
    }

    @NotNull
    public List<BuildMessage1> translate(@NotNull SRunningBuild runningBuild,
                                         @NotNull BuildMessage1 buildMessage,
                                         @NotNull ServiceMessage serviceMessage) {
        // prepare result
        List<BuildMessage1> result = new Vector<BuildMessage1>();
        result.add(buildMessage);

        // prepare build history
        List<SFinishedBuild> history = runningBuild.getBuildType().getHistory();

        try {
            String key = serviceMessage.getAttributes().get("key");
            String value = serviceMessage.getAttributes().get("value");
            dotTraceResultsComparer comparer = null;

            if (key != null && value != null) {
                // parse value
                BigDecimal bdValue = parseValue(history, key, value);

                // write results to maps for comparison
                String keyM = key.split(":")[0];
                if (key.contains(":baseOwnTime")) {
                    myPerfBaseOwnTime.put(keyM, bdValue.intValue());
                } else if (key.contains(":baseTotalTime")) {
                    myPerfBaseTotalTime.put(keyM, bdValue.intValue());
                } else if (key.contains(":reportOwnTime")) {
                    myPerfReportOwnTime.put(keyM, bdValue.intValue());
                } else if (key.contains(":reportTotalTime")) {
                    myPerfReportTotalTime.put(keyM, bdValue.intValue());
                }

                // after all results are ready, generate text report for build log
                if (key.contains(dotTraceRunnerConstants.DT_END_PROF_SERVICE_MESSAGE_NAME)) {
                    comparer = new dotTraceResultsComparer(
                            myPerfBaseTotalTime, myPerfBaseOwnTime, myPerfReportTotalTime, myPerfReportOwnTime);
                    result.clear();
                    result.add(new BuildMessage1(buildMessage.getSourceId(), buildMessage.getTypeId(),
                            buildMessage.getStatus(), buildMessage.getTimestamp(), comparer.getComparisonAsString()));
                }

                // save statistic value
                if (key.contains(":baseOwnTime") || key.contains(":baseTotalTime")) {
                    if (bdValue.intValue() == -1 || bdValue.intValue() == -2) {bdValue = new BigDecimal(0);}
                }
                myStorage.publishValue(key, runningBuild.getBuildId(), bdValue);
            }

            // finally
            if (comparer != null) {
                if (comparer.isSuccessful()) {
                    result.add(new BuildMessage1(buildMessage.getSourceId(), buildMessage.getTypeId(),
                            buildMessage.getStatus(), buildMessage.getTimestamp(),
                            "SUCCESS! Profiled methods do not exceed specified thresholds"));
                }
                else {
                    result.add(new BuildMessage1(buildMessage.getSourceId(), buildMessage.getTypeId(),
                            buildMessage.getStatus(), buildMessage.getTimestamp(),
                            "FAILED! Some of the performance thresholds were exceeded"));

                    runningBuild.addBuildProblem(BuildProblemData.createBuildProblem(
                            Hash.calc("ThrExceeded") +
                                "", "dotTraceProblemType",
                                "Performance thresholds exceeded: " +
                                        getFailedThresholdsCount(comparer.getComparisonAsMap()).toString()));
                }
            }
            return result;
        } catch (Exception e) {

            String message = buildErrorMessage(serviceMessage, e);
            Loggers.SERVER.info("Error processing service message for build " + LogUtil.describe(runningBuild) + ": " + message);
            Loggers.SERVER.debug("Error processing service message for build " + LogUtil.describe(runningBuild) + ": " + message, e);
            return Collections.singletonList(DefaultMessagesInfo.createTextMessage(message, Status.WARNING));
        }
    }

    @NotNull
    private BigDecimal parseValue(List<SFinishedBuild> buildHistory, String key, String value) {
        try {
            if (key.contains(":baseTotalTime") || key.contains(":baseOwnTime")) {
                // check whether they must be ignored
                if (value.equals("0")) {
                    return BigDecimal.valueOf(-1);  // -1 is an indicator of "ignore value"
                }

                // try to find value (+ variation) in the last successful build
                if (value.toUpperCase().contains("L")){
                    String keyDb = key.replaceAll(":base", ":report");
                    BigDecimal lastSuccessValue = getLastSuccessBuildValue(buildHistory, keyDb);
                    if (lastSuccessValue != null){
                        int variation = Integer.parseInt(value.replaceAll("L", ""));
                        return new BigDecimal(lastSuccessValue.intValue() +
                                lastSuccessValue.intValue() * variation * 0.01);
                    }
                    return BigDecimal.valueOf(-2);  // -2 is an indicator of "no history"
                }

                // TODO: try to calculate average value based on all successful builds
            }

            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse decimal value: \"" + value + "\"");
        }
    }

    private BigDecimal getLastSuccessBuildValue(List<SFinishedBuild> buildHistory, String key){

        List<SFinishedBuild> successfulBuilds = new Vector<SFinishedBuild>();

        // take only successful builds
        for (SFinishedBuild entry : buildHistory) {
            if (entry.getBuildStatus().isSuccessful()){
                successfulBuilds.add(entry);
            }
        }

        // find the last one that contains the key
        for (SFinishedBuild entry : successfulBuilds) {
            Map<String, BigDecimal> values = myStorage.getValues(entry);
            if (values.containsKey(key)){
                return values.get(key);
            }
        }
        return null;
    }

    private Integer getFailedThresholdsCount(Map<String, ProfilingResult> comparisonResult){
        int result = 0;
        for (Map.Entry<String,ProfilingResult> entry : comparisonResult.entrySet()) {
            if (!entry.getValue().isSuccessful()){
                result++;
            }
        }
        return result;
    }

    private String buildErrorMessage(final ServiceMessage serviceMessage, Exception e) {
        final String key = serviceMessage.getAttributes().get("key");
        final String value = serviceMessage.getAttributes().get("value");
        final String messageName = serviceMessage.getMessageName();
        String exceptionMessage = e.getMessage();
        if (exceptionMessage != null) {
            exceptionMessage = ". " + exceptionMessage;
        }
        return "Unable to process service message: \"[" + messageName + " key='" + key + "' value='" + value + "']\"" + exceptionMessage;
    }

    @NotNull
    public String getServiceMessageName() {
        return dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME;
    }

}
