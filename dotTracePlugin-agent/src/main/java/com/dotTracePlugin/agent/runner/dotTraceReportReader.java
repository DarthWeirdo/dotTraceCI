package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.agent.model.ProfiledMethods;
import com.dotTracePlugin.agent.tools.XMLConverter;
import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.agent.SimpleBuildLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, ProfiledMethod> result = new HashMap<String, ProfiledMethod>();
        XMLConverter converter = new XMLConverter();
        String resultsPath = new File(myDotTracePath, dotTraceRunnerConstants.DT_REPORTER_RESULTS).getPath();

        myLogger.message("Reading profiling results...");
        ProfiledMethods methodList =
//                converter.convertFromXMLToObject(resultsPath);
                (ProfiledMethods) converter.convertFromXMLToObject(resultsPath, ProfiledMethods.class);

        for (ProfiledMethod method : methodList.getMethods()) {
            result.put(method.getFQN(), method);
        }

        return result;
    }
}
