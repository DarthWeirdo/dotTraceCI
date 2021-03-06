package com.dotTracePlugin.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class dotTraceRunnerConstants {
    public static final String DT_RUN_TYPE = "dotTrace-runner";
    public static final String DT_PROFILER_BINARY = "ConsoleProfiler.exe";
    public static final String DT_REPORTER_BINARY = "Reporter.exe";
    public static final String DT_RUN_SCRIPT = "start.bat";
    public static final String DT_SNAPSHOT = "snapshot.dtp";
    public static final String DT_REPORTER_CONFIG = "reporter_pattern.xml";
    public static final String DT_REPORTER_RESULTS = "report_result.xml";
    public static final String DT_TEMP_PROFILING_CONFIG = "temp_config.xml";
//    public static final String DT_SERVICE_MESSAGE_NAME = "buildStatisticValue";
    public static final String DT_SERVICE_MESSAGE_NAME = "dotTraceStatisticValue";
    public static final String DT_END_PROF_SERVICE_MESSAGE_NAME = "profilingFinished";
    public static final String CHECKOUTDIR_PLACEHOLDER = "%CHECKOUTDIR%";

    public static final String PARAM_DOTTRACE_PATH = "com.dotTracePlugin.dotTracePath";
    public static final String PARAM_PROFILING_CONFIG_PATH = "com.dotTracePlugin.ProfilingConfigPath";
    public static final String PARAM_TEMP_PATH = "com.dotTracePlugin.TempPath";
    public static final String PARAM_THRESHOLDS = "com.dotTracePlugin.Thresholds";
//    public static final String PARAM_ON_EXC_THRESHOLDS = "com.dotTracePlugin.OnExcThresholds";
//    public static final String ON_EXC_FAIL = "com.dotTracePlugin.OnExcThresholds.OnExcFail";
//    public static final String ON_EXC_PROBLEMS = "com.dotTracePlugin.OnExcThresholds.OnExcProblems";
    public static final String PARAM_PUBLISH_SNAPSHOT = "com.dotTracePlugin.PublishSnapshot";
    public static final String ALWAYS = "com.dotTracePlugin.PublishSnapshot.Always";
    public static final String EXC_THRESHOLDS = "com.dotTracePlugin.PublishSnapshot.ExcThresholds";
    public static final String NEVER = "com.dotTracePlugin.PublishSnapshot.Never";



//    public String getOnExcThresholdsType() {
//        return PARAM_ON_EXC_THRESHOLDS;
//    }
//
//    public Map<String, String> getOnExcThresholdsTypeValues() {
//        final Map<String, String> result = new LinkedHashMap<String, String>();
//        result.put(ON_EXC_FAIL, "Fail build");
//        result.put(ON_EXC_PROBLEMS, "Finish build with problems");
//        return result;
//    }

    public String getPublishSnapshotType() {
        return PARAM_PUBLISH_SNAPSHOT;
    }

    public Map<String, String> getPublishSnapshotTypeValues() {
        final Map<String, String> result = new LinkedHashMap<String, String>();
        result.put(ALWAYS, "Always");
        result.put(EXC_THRESHOLDS, "On exceeding thresholds");
        result.put(NEVER, "Never");
        return result;
    }
}
