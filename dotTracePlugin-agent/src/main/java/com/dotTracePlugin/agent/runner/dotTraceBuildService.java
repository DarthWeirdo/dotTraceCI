package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.CompareResult;
import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceBuildService extends BuildServiceAdapter {
    private Map<String, ProfiledMethod> perfThresholds = new HashMap<String, ProfiledMethod>();
    private Map<String, ProfiledMethod> perfResults = new HashMap<String, ProfiledMethod>();


    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        final Map<String, String> runParameters = getRunnerParameters();
        final String dotTracePath = runParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        final dotTraceProfilerCommandLineBuilder profilerCmdBuilder =
                new dotTraceProfilerCommandLineBuilder(this.getBuild(), runParameters, getLogger());



        return new ProgramCommandLine() {
            @NotNull
            public String getExecutablePath() throws RunBuildException {
                return profilerCmdBuilder.getExecutablePath();
            }

            @NotNull
            public String getWorkingDirectory() throws RunBuildException {
                return dotTracePath;
            }

            @NotNull
            public List<String> getArguments() throws RunBuildException {
                return profilerCmdBuilder.getArguments();
            }

            @NotNull
            public Map<String, String> getEnvironment() throws RunBuildException {
                return getEnvironmentVariables();
            }
        };
    }

    @Override
    public void afterInitialized() throws RunBuildException{
        super.afterInitialized();

        final Map<String, String> runParameters = getRunnerParameters();
        final dotTraceReporterConfigBuilder reporterConfigBuilder =
                new dotTraceReporterConfigBuilder(runParameters.get(dotTraceRunnerConstants.PARAM_THRESHOLDS),
                        runParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH),
                        getLogger());
        try {
            reporterConfigBuilder.makeConfig();
            perfThresholds = reporterConfigBuilder.getTresholdValues();
        } catch (IOException e) {
            getLogger().message("Unable to create reporter config file");
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public BuildFinishedStatus getRunResult(final int exitCode) {

        final Map<String, String> runParameters = getRunnerParameters();
        final dotTraceReportReader resultsReader =
                new dotTraceReportReader(runParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH), getLogger());

        // what to do with build on exceeding thresholds
        BuildFinishedStatus onExcThrResult = BuildFinishedStatus.FINISHED_FAILED;
        final String onExcThr = runParameters.get(dotTraceRunnerConstants.PARAM_ON_EXC_THRESHOLDS);
        if (dotTraceRunnerConstants.ON_EXC_PROBLEMS.equals(onExcThr))
            onExcThrResult = BuildFinishedStatus.FINISHED_WITH_PROBLEMS;

        // publish snapshot to artifacts?
        String publishSnapshotMsg = String.format("##teamcity[publishArtifacts '%s* => dotTraceSnapshot.zip']",
                new File(runParameters.get(dotTraceRunnerConstants.PARAM_TEMP_PATH),
                        dotTraceRunnerConstants.DT_SNAPSHOT).getPath());
        final String publishSnapshot = runParameters.get(dotTraceRunnerConstants.PARAM_PUBLISH_SNAPSHOT);


        if (exitCode != 0) {
            getLogger().message("dotTrace plugin was unable to finish some of the steps. See agent log for details");
            return BuildFinishedStatus.FINISHED_WITH_PROBLEMS;
        }
        else {
            try {
                perfResults = resultsReader.readPerfResults();
                dotTraceComparer comparer = new dotTraceComparer(perfThresholds, perfResults);

                // Write comparison results to build log
                getLogger().message(comparer.getComparisonAsString());
                getLogger().logMessage(DefaultMessagesInfo.createTextMessage(
                        comparer.getComparisonAsServiceMessage())
                        .updateTags(DefaultMessagesInfo.TAG_INTERNAL));

                if (comparer.isSuccessful()) {
                    getLogger().message("SUCCESS! Profiled methods do not exceed specified thresholds");

                    // Publishing snapshot to artifacts
                    if (dotTraceRunnerConstants.ALWAYS.equals(publishSnapshot)) {
                        getLogger().message("For more details, examine the snapshot in Artifacts\\dotTraceSnapshot.zip");
                        getLogger().logMessage(DefaultMessagesInfo.createTextMessage(
                                publishSnapshotMsg).updateTags(DefaultMessagesInfo.TAG_INTERNAL));
                    }

                    return BuildFinishedStatus.FINISHED_SUCCESS;
                } else {

                    getLogger().message("FAILED! Some of the specified thresholds were exceeded");

                    // Publishing snapshot to artifacts
                    if (dotTraceRunnerConstants.EXC_THRESHOLDS.equals(publishSnapshot)) {
                        getLogger().logMessage(DefaultMessagesInfo.createTextMessage(
                                publishSnapshotMsg).updateTags(DefaultMessagesInfo.TAG_INTERNAL));
                        getLogger().message("For more details, examine the snapshot in Artifacts\\dotTraceSnapshot");
                    }

                    return onExcThrResult;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getLogger().message("dotTrace plugin was unable to finish some of the steps. See agent log for details");
        return BuildFinishedStatus.FINISHED_WITH_PROBLEMS;
    }
}
