<%@ page import="com.dotTracePlugin.common.dotTraceRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="runnerConst" scope="request" class="com.dotTracePlugin.common.dotTraceRunnerConstants"/>
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

<div class="parameter">
    On exceeding thresholds: <strong><c:forEach var="type" items="${runnerConst.onExcThresholdsTypeValues}"><c:if test="${type.key == propertiesBean.properties[runnerConst.onExcThresholdsType]}"><c:out value="${type.value}"/></c:if></c:forEach></strong>
</div>

<div class="parameter">
    Publish performance snapshot to artifacts: <strong><c:forEach var="type" items="${runnerConst.publishSnapshotTypeValues}"><c:if test="${type.key == propertiesBean.properties[runnerConst.publishSnapshotType]}"><c:out value="${type.value}"/></c:if></c:forEach></strong>
</div>