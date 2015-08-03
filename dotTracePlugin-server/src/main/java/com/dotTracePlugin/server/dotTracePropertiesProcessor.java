package com.dotTracePlugin.server;


import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.util.StringUtils;

import java.io.File;
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
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH, "The path must be specified"));
        }

        if (StringUtil.isEmptyOrSpaces(properties.get(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH))) {
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH, "The path must be specified"));
        }

        File f = new File(properties.get(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH));
        if(!f.exists() && !f.isDirectory()) {
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH, "The config file does not exist"));}

        if (StringUtil.isEmptyOrSpaces(properties.get(dotTraceRunnerConstants.PARAM_TEMP_PATH))) {
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_TEMP_PATH, "The path must be specified"));
        }

        if (StringUtil.isEmptyOrSpaces(properties.get(dotTraceRunnerConstants.PARAM_THRESHOLDS))) {
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_THRESHOLDS, "Threshold values must be specified"));
        }

        if (!checkThresholdValuesValidity(properties)){
            result.add(new InvalidProperty(dotTraceRunnerConstants.PARAM_THRESHOLDS, "Invalid threshold values format"));
        }

        return result;
    }

    private boolean checkThresholdValuesValidity(Map<String, String> properties){
        String myThresholdValuesString = properties.get(dotTraceRunnerConstants.PARAM_THRESHOLDS);
        String possibleChars = "FALfal";
        try {
            String[] lines = myThresholdValuesString.split("\\r?\\n");

            for (String line : lines) {
                String[] splitLine = line.split("\\s+");

                if (splitLine.length < 3)
                    return false;

                if ((splitLine[0].length() - splitLine[0].replace(".", "").length()) != 2)
                    return false;

                for (int i = 1; i <= 2; i++) {
                    if (!isInteger(splitLine[i])) {
                        if (possibleChars.indexOf(splitLine[i].charAt(0)) >= 0){
                            if (!isInteger(splitLine[i].substring(1, splitLine[i].length())))
                                return false;
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}
