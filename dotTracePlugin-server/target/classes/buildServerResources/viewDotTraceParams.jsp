<%@ page import="com.dotTracePlugin.common.dotTraceRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    dotTrace path: <strong><props:displayValue name="<%=dotTraceRunnerConstants.PARAM_DOTTRACE_PATH%>" emptyValue="default"/></strong>
</div>

<div class="parameter">
    Profiling config file path: <strong><props:displayValue name="<%=dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH%>" emptyValue="default"/></strong>
</div>

<div class="parameter">
    Temp directory path: <strong><props:displayValue name="<%=dotTraceRunnerConstants.PARAM_TEMP_PATH%>" emptyValue="default"/></strong>
</div>

<div class="parameter">
    Threshold values: <strong><props:displayValue name="<%=dotTraceRunnerConstants.PARAM_THRESHOLDS%>" emptyValue="default"/></strong>
</div>