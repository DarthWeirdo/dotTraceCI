package com.dotTracePlugin.agent;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public class dotTraceRunner implements CommandLineBuildServiceFactory
{
    public static final Logger myLog = Logger.getLogger(dotTraceRunner.class);

    public CommandLineBuildService createService() {
        return new dotTraceBuildService();
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo(){
        return new dotTraceRunnerInfo();
    }
}
