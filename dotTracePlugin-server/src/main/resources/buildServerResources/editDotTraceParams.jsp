<%@ page import="com.dotTracePlugin.common.dotTraceRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<l:settingsGroup title="Profiling Options">
    <tr>
        <th><label for="com.dotTracePlugin.common.dotTracePath">dotTrace path: <l:star/></label></th>
        <td>
            <props:textProperty name="<%=dotTraceRunnerConstants.PARAM_DOTTRACE_PATH%>" className="longField" maxlength="1024" />
            <span class="smallNote">Enter local path (for a particular agent) to the dotTrace directory</span>
        </td>
    </tr>
</l:settingsGroup>