<%@ page import="com.dotTracePlugin.common.dotTraceRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="runnerConst" scope="request" class="com.dotTracePlugin.common.dotTraceRunnerConstants"/>

<l:settingsGroup title="Profiling Options">
    <tr>
        <th><label for="com.dotTracePlugin.common.dotTracePath">dotTrace path: <l:star/></label></th>
        <td>
            <props:textProperty name="<%=dotTraceRunnerConstants.PARAM_DOTTRACE_PATH%>" className="longField" maxlength="1024" />
            <span class="smallNote">Enter local path (for a particular agent) to the dotTrace directory</span>
        </td>
    </tr>
    <tr>
        <th><label for="com.dotTracePlugin.common.ProfilingConfigPath">Profiling config file path: <l:star/></label></th>
        <td>
            <props:textProperty name="<%=dotTraceRunnerConstants.PARAM_PROFILING_CONFIG_PATH%>" className="longField" maxlength="1024" />
            <span class="smallNote">Enter local path (for a particular agent) to the file with profiling configuration</span>
        </td>
    </tr>
    <tr>
        <th><label for="com.dotTracePlugin.common.TempPath">Temp directory path: <l:star/></label></th>
        <td>
            <props:textProperty name="<%=dotTraceRunnerConstants.PARAM_TEMP_PATH%>" className="longField" maxlength="1024" />
            <span class="smallNote">Enter local path (for a particular agent) to the temporary directory for storing performance snapshots</span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Performance Thresholds">
    <tr>
        <th><label for="com.dotTracePlugin.common.Thresholds">Threshold values: </label></th>
        <td>
            <props:multilineProperty name="<%=dotTraceRunnerConstants.PARAM_THRESHOLDS%>" className="longField" cols="30" rows="10" expanded="true" linkTitle="Enter performance thresholds"/>
            <span class="smallNote">Newline-separated list of methods and their performance thresholds.
                <br/>Pattern: <b>Namespace.Class.Method TotalTime OwnTime Calls Instances</b>, where
                <br/><b>TotalTime</b> - execution time of the method's call subtree in ms.
                <br/><b>OwnTime</b> - method's own execution time in ms.
                <br/><b>Calls</b> - number of calls.
                <br/><b>Instances</b> - number of method instances (if it was called from different places).
                <br/>Use the <b>0</b> value to ignore a certain parameter.
                <br/>
                <br/>E.g., the build step will fail if <b>Test1</b> total time exceeds 100 ms:
                <br/><b>IntegrationTests.MainTests.Test1 100 0 0 0</b>
                <br/>
            </span>
        </td>
    </tr>

    <tr>
        <th><label for="com.dotTracePlugin.OnExcThresholds">On exceeding thresholds: </label></th>
        <td>
            <props:selectProperty name="<%=dotTraceRunnerConstants.PARAM_ON_EXC_THRESHOLDS%>">
                <c:forEach var="type" items="${runnerConst.onExcThresholdsTypeValues}">
                    <props:option value="${type.key}"><c:out value="${type.value}"/></props:option>
                </c:forEach>
            </props:selectProperty>
            <%--<span class="smallNote">Select SSH transfer protocol to use</span>--%>
        </td>
    </tr>

    <tr>
        <th><label for="com.dotTracePlugin.PublishSnapshots">Publish performance snapshot to artifacts: </label></th>
        <td>
            <props:selectProperty name="<%=dotTraceRunnerConstants.PARAM_PUBLISH_SNAPSHOT%>">
                <c:forEach var="type" items="${runnerConst.publishSnapshotTypeValues}">
                    <props:option value="${type.key}"><c:out value="${type.value}"/></props:option>
                </c:forEach>
            </props:selectProperty>
                <%--<span class="smallNote">Select SSH transfer protocol to use</span>--%>
        </td>
    </tr>
</l:settingsGroup>