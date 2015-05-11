package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceBuildService extends BuildServiceAdapter {

//    private final dotTraceReporterConfigBuilder myReporterConfigBuilder;
//    private final Map<String, String> myRunParameters;

    public dotTraceBuildService() {
//        super();
//        myRunParameters = getRunnerParameters();
//        myReporterConfigBuilder =
//                new dotTraceReporterConfigBuilder(myRunParameters.get(dotTraceRunnerConstants.PARAM_THRESHOLDS),
//                        myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH),
//                        getLogger());
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        final Map<String, String> runParameters = getRunnerParameters();
        final String dotTracePath = runParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        final dotTraceProfilerCommandLineBuilder profilerCmdBuilder =
                new dotTraceProfilerCommandLineBuilder(runParameters, getLogger());

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
        } catch (IOException e) {
            getLogger().message("Unable to create reporter config.");
            e.printStackTrace();
        }
    }
}
