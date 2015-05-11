package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceBuildService extends BuildServiceAdapter {
    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        final Map<String, String> runParameters = getRunnerParameters();
        final String dotTracePath = runParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        final dotTraceProfilerCommandLineBuilder ProfilerCmdBuilder =
                new dotTraceProfilerCommandLineBuilder(runParameters, getLogger());

        return new ProgramCommandLine() {
            @NotNull
            public String getExecutablePath() throws RunBuildException {
                return ProfilerCmdBuilder.getExecutablePath();
            }

            @NotNull
            public String getWorkingDirectory() throws RunBuildException {
                return dotTracePath;
            }

            @NotNull
            public List<String> getArguments() throws RunBuildException {
                List<String> arguments = new Vector<String>();
                return arguments;
            }

            @NotNull
            public Map<String, String> getEnvironment() throws RunBuildException {
                return getEnvironmentVariables();
            }
        };
    }
}
