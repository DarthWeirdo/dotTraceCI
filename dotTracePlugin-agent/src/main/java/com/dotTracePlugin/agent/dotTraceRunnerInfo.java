package com.dotTracePlugin.agent;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceRunnerInfo implements AgentBuildRunnerInfo {
    @NotNull
    public String getType() {
        return dotTraceRunnerConstants.DT_RUN_TYPE;
    }

    public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
        if (!agentConfiguration.getSystemInfo().isWindows()) {
            try {
                throw new RunBuildException("dotTracePlugin can run only on Windows agents");
            } catch (RunBuildException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
