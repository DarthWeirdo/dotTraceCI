package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.agent.model.ProfiledMethodList;
import com.dotTracePlugin.agent.model.ReportPattern;
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
 * Created by Alexey.Totin on 5/14/2015.
 */
public class dotTraceReportReader {
    private String myDotTracePath;
    private Map<String, ProfiledMethod> myThresholdValuesMap = new HashMap<String, ProfiledMethod>();;
    private SimpleBuildLogger myLogger;

    public dotTraceReportReader(String dotTracePath, SimpleBuildLogger logger) {
        myDotTracePath = dotTracePath;
        myLogger = logger;
    }


    public Map<String, ProfiledMethod> readPerfResults() throws IOException {

        XMLConverter converter = new XMLConverter();
        String resultsPath = new File(myDotTracePath, dotTraceRunnerConstants.DT_REPORTER_RESULTS).getPath();
        myLogger.message("Results report path: " + resultsPath);

        myLogger.message("Reading profiling results:");
        ProfiledMethodList methodList =
//                converter.convertFromXMLToObject(resultsPath);
                (ProfiledMethodList) converter.convertFromXMLToObject(resultsPath, ProfiledMethodList.class);

        for (ProfiledMethod method : methodList.getMethods()) {
            myLogger.message(method.getFQN());
        }

        return null;
    }
}
