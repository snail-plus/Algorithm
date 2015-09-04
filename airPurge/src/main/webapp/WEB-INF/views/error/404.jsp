<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%response.setStatus(200);%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 - 页面不存在</title>
</head>
<script type="text/javascript">
    function back() {
        window.parent.location = "${ctx}";
    }
</script>
<body>
<h2>404 - 页面不存在.</h2>

<p><a href="javascript:back();">返回首页</a></p>
</body>
</html>