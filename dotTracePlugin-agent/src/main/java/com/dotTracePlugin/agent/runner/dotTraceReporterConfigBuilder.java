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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Alexey.Totin on 5/11/2015.
 */
public class dotTraceReporterConfigBuilder {
    private String myThresholdValues;
    private String myDotTracePath;
    private final SimpleBuildLogger myLogger;
    public Map<String, ProfiledMethod> ThresholdValues;

    public dotTraceReporterConfigBuilder(String thresholdValues, String dotTracePath, SimpleBuildLogger logger) {
        myThresholdValues = thresholdValues;
        myDotTracePath = dotTracePath;
        myLogger = logger;
        ThresholdValues = new HashMap<String, ProfiledMethod>();
    }

    public void makeConfig() throws IOException {

        // Parse threshold values string
        myLogger.message("Parsing threshold values..." + myThresholdValues);
        String[] lines = myThresholdValues.split("\\r?\\n");

        for (String line : lines) {
            String[] splitLine = line.split("\\s+");
            ProfiledMethod method = new ProfiledMethod(
                    splitLine[0], splitLine[1], splitLine[2], splitLine[3], splitLine[4]);
            ThresholdValues.put(splitLine[0], method);
            myLogger.message("Add line: " + splitLine[0]);
        }

        // Generate XML with reporter configuration
        myLogger.message("Generating XML report configuration...");
        XMLConverter converter = new XMLConverter();
        String configPath = new File(myDotTracePath, dotTraceRunnerConstants.DT_REPORTER_CONFIG).getPath();
        myLogger.message("Report config path: " + configPath);

        List<ReportPattern> patterns = new Vector<ReportPattern>();

        for (Map.Entry<String,ProfiledMethod> entry : ThresholdValues.entrySet()) {
            patterns.add(new ReportPattern());
            patterns.get(patterns.size()-1).setPattern(entry.getValue().FQN);
            myLogger.message("Add pattern: " + patterns.get(patterns.size() - 1).getPattern());
            myLogger.message("from map: " + entry.getKey());
        }

        ReportPatterns reportPatterns = new ReportPatterns();
        reportPatterns.setPatterns(patterns);
        converter.convertFromObjectToXML(reportPatterns, configPath);
    }
}
