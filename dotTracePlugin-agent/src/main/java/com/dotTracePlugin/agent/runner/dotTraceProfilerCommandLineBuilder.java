package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.SimpleBuildLogger;
import jetbrains.buildServer.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceProfilerCommandLineBuilder {
    private final Map<String, String> myRunParameters;
    private final SimpleBuildLogger myLogger;

    public dotTraceProfilerCommandLineBuilder(final Map<String, String> runParameters, SimpleBuildLogger logger) {
        myRunParameters = runParameters;
        myLogger = logger;
    }

    @NotNull
    public String getExecutablePath() throws RunBuildException {
        String dotTraceRelativePath = myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        if (StringUtil.isEmpty(dotTraceRelativePath)) {
            throw new RunBuildException("dotTrace path is not specified");
        }
        String result = new File(dotTraceRelativePath, dotTraceRunnerConstants.DT_RUN_SCRIPT).getPath();
        myLogger.message("Searching for profiling script: " + result);
        return result;
    }

}
