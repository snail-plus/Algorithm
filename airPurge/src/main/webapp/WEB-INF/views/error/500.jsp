<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>
<%@ include file="/taglibs.jsp" %>
<%
    Throwable ex = null;
    if (exception != null)
        ex = exception;
    if (request.getAttribute("javax.servlet.error.exception") != null)
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

    //记录日志
    Logger logger = LoggerFactory.getLogger("500.jsp");
    logger.error(ex.getMessage(), ex);
%>

<!DOCTYPE html>
<html>
<head>
    <title>发生错误</title>
</head>
<script type="text/javascript">
    function back() {
        window.parent.location = "${ctx}";
    }
</script>
<body>
<h2>发生错误，请稍后重新操作!</h2>

<p><a href="javascript:back();">返回首页</a></p>
</body>
</html>
