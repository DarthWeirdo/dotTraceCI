package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.SimpleBuildLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceProfilerCommandLineBuilder {
    private AgentRunningBuild myRunningBuild;
    private final Map<String, String> myRunParameters;
    private final SimpleBuildLogger myLogger;
    private final dotTraceScriptBuilder myScriptBuilder;

    public dotTraceProfilerCommandLineBuilder(final AgentRunningBuild runningBuild,
                                              final Map<String, String> runParameters, SimpleBuildLogger logger) {
        myRunningBuild = runningBuild;
        myRunParameters = runParameters;
        myLogger = logger;

        // replace relative path with absolute in the profiling config file
        String configPath = myRunParameters.get(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH);
        String dotTracePath = myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        Map<String, String> confMap = new HashMap<String, String>();
        confMap.put(dotTraceRunnerConstants.CHECKOUTDIR_PLACEHOLDER, myRunningBuild.getCheckoutDirectory().toString());
        dotTraceProfilerConfigReader configReader = new dotTraceProfilerConfigReader();
        configReader.ReplacePlaceholdersInFile(
                confMap,
                configPath,
                new File(dotTracePath, dotTraceRunnerConstants.DT_TEMP_PROFILING_CONFIG).getPath());

        // create start.bat script
        myScriptBuilder = new dotTraceScriptBuilder(myRunParameters);
        myScriptBuilder.saveScriptToDisk();
    }

    @NotNull
    public String getExecutablePath() throws RunBuildException {
        String dotTraceRelativePath = myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        if (StringUtil.isEmpty(dotTraceRelativePath)) {
            throw new RunBuildException("dotTrace path is not specified");
        }
        String result = new File(dotTraceRelativePath, dotTraceRunnerConstants.DT_RUN_SCRIPT).getPath();
        return result;
    }

    @NotNull
    public List<String> getArguments() throws RunBuildException {
        List<String> arguments = new Vector<String>();
        arguments.add(0, "");
        return arguments;
    }

}
