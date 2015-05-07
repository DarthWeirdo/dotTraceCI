package com.dotTracePlugin.server;


import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/6/2015.
 */
public class dotTracePropertiesProcessor implements PropertiesProcessor {

    public Collection<InvalidProperty> process(Map<String, String> properties) {
        Collection<InvalidProperty> result = new HashSet<InvalidProperty>();
        if (StringUtil.isEmptyOrSpaces(properties.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH))) {
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH, "The path must be specified."));
        }
        return result;
    }
}
