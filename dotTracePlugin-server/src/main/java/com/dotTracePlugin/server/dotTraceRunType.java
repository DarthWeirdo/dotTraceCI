package com.dotTracePlugin.server;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.RunningBuild;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.statistics.build.BuildDataStorage;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTraceRunType extends RunType {

    private final PluginDescriptor myDescriptor;

    public dotTraceRunType(@NotNull final RunTypeRegistry registry,
                           @NotNull final PluginDescriptor descriptor) {
        registry.registerRunType(this);
        myDescriptor = descriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return dotTraceRunnerConstants.DT_RUN_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "dotTrace Profiler";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Find performance bottlenecks in .NET apps by profiling integration tests";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new dotTracePropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> properties) {
                return super.process(properties);
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return  myDescriptor.getPluginResourcesPath() + "editDotTraceParams.jsp";
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return  myDescriptor.getPluginResourcesPath() + "viewDotTraceParams.jsp";
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return new HashMap<String, String>();
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("dotTrace path: ").append(parameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH)).append("\n");
        sb.append("Profiling config path:").append(parameters.get(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH));
        return sb.toString();
    }
}
