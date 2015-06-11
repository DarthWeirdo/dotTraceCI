package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.common.dotTraceRunnerConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/22/2015.
 */
public class dotTraceScriptBuilder {
    private final Map<String, String> myRunParameters;
    private StringBuilder myScript = new StringBuilder();

    public dotTraceScriptBuilder(Map<String, String> runParameters) {
        myRunParameters = runParameters;
        String dotTracePath = myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        String consoleProfilerPath = new File(dotTracePath, dotTraceRunnerConstants.DT_PROFILER_BINARY).getPath();
        String configPath = new File(dotTracePath, dotTraceRunnerConstants.DT_TEMP_PROFILING_CONFIG).getPath();
        String tempPath = myRunParameters.get(dotTraceRunnerConstants.PARAM_TEMP_PATH);
        String snapshotPath = new File(tempPath, dotTraceRunnerConstants.DT_SNAPSHOT).getPath();
        String reporterPath = new File(dotTracePath, dotTraceRunnerConstants.DT_REPORTER_BINARY).getPath();
        String reporterPatternPath = dotTraceRunnerConstants.DT_REPORTER_CONFIG;
        String reporterResultsPath = dotTraceRunnerConstants.DT_REPORTER_RESULTS;

        String scriptLine = String.format("del /F /Q \"%s*\" \n", snapshotPath);
        myScript.append(scriptLine);
        scriptLine = String.format("\"%s\" \"%s\" \"%s\" \n",
                consoleProfilerPath, configPath, snapshotPath);
        myScript.append(scriptLine);
        scriptLine = String.format("\"%s\" /reporting \"%s\" %s %s",
                reporterPath, snapshotPath, reporterPatternPath, reporterResultsPath);
        myScript.append(scriptLine);
    }

    public void saveScriptToDisk() {
        String dotTraceRelativePath = myRunParameters.get(dotTraceRunnerConstants.PARAM_DOTTRACE_PATH);
        String scriptPath = new File(dotTraceRelativePath, dotTraceRunnerConstants.DT_RUN_SCRIPT).getPath();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(scriptPath, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            writer.println(myScript.toString());
            writer.close();
        }
    }
}
