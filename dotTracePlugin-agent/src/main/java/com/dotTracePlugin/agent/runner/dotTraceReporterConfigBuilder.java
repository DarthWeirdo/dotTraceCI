package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.agent.model.ReportPattern;
import com.dotTracePlugin.agent.model.ReportPatterns;
import com.dotTracePlugin.agent.tools.XMLConverter;
import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.agent.SimpleBuildLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */
public class dotTraceReporterConfigBuilder {
    private String myThresholdValuesString;
    private String myDotTracePath;
    private final SimpleBuildLogger myLogger;
    private Map<String, ProfiledMethod> myThresholdValuesMap = new HashMap<String, ProfiledMethod>();

    public dotTraceReporterConfigBuilder(String thresholdValues, String dotTracePath, SimpleBuildLogger logger) {
        myThresholdValuesString = thresholdValues;
        myDotTracePath = dotTracePath;
        myLogger = logger;
    }

    public Map<String, ProfiledMethod> makeConfig() throws IOException {

        // Parse threshold values string
        myLogger.message("Parsing threshold values..." + myThresholdValuesString);
        String[] lines = myThresholdValuesString.split("\\r?\\n");

        for (String line : lines) {
            String[] splitLine = line.split("\\s+");
            ProfiledMethod method = new ProfiledMethod(
                    "x",splitLine[0], splitLine[1], splitLine[2], splitLine[3], splitLine[4]);
            myThresholdValuesMap.put(splitLine[0], method);
        }

        // Generate XML with reporter configuration
        myLogger.message("Generating XML report configuration...");
        XMLConverter converter = new XMLConverter();
        String configPath = new File(myDotTracePath, dotTraceRunnerConstants.DT_REPORTER_CONFIG).getPath();
        myLogger.message("Report config path: " + configPath);

        List<ReportPattern> patterns = new Vector<ReportPattern>();

        for (Map.Entry<String,ProfiledMethod> entry : myThresholdValuesMap.entrySet()) {
            patterns.add(new ReportPattern());
            patterns.get(patterns.size()-1).setPattern(entry.getValue().getFQN());
        }

        ReportPatterns reportPatterns = new ReportPatterns();
        reportPatterns.setPatterns(patterns);
        converter.convertFromObjectToXML(reportPatterns, configPath);

        // Return config - the map with threshold values set by user
        return myThresholdValuesMap;
    }
}
