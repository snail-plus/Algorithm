<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录</title>
<jsp:include page="/meta.jsp"/>
<link type="text/css" rel="stylesheet" href="${css}/loginmain.css"/>
<style type="text/css">
body, html{width: 100%;height: 100%;}
.login_bj{background: url(${img}/bj.jpg) no-repeat;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=${img}/bj.jpg, sizingMethod=scale);
 }
</style>
</head>
<body>
<div class="fixed-bj"></div>

<div class="login_muto">
	<table cellspacing="0" cellpadding="0" width="100%" height="100%">
    	<tr>
        	<td>
            	<div class="aaa" style="position:relative;">
<%--                	 <img src="${img}/r-center-img.png" />
 --%>                    <div class="r-login">
                       <div class="w-login-title">
                      		 欢迎登录空气净化系统
                       </div>
                         <form id="dialogForm" method="post" action="${ctx}/login">
                       <div class="logo-input">
                       		 <p><font>用户名：</font><input type="text" name="username" id="username" /></p>
                             <p><font>密&nbsp;&nbsp; 码：</font><input type="password" name="password" id="password" /></p>
                             <div class="remember"><font>&nbsp;</font><input type="checkbox" id="rememberUserName" checked="checked" /><span class="middle">记住用户名</span></div>
                             <div class="error-inf" id="error" color="#ff0000">
	                             <c:choose>
		                                <c:when
		                                        test="${shiroLoginFailure eq 'org.apache.shiro.authc.AccountException'}">
		                                        	用户名不存在
		                                </c:when>
		                                <c:when
		                                        test="${shiroLoginFailure eq 'org.apache.shiro.authc.IncorrectCredentialsException'}">
		                                        	用户名或密码不正确
		                                </c:when>
		                               <c:when
			                                        test="${shiroLoginFailure eq 'com.whtriples.exception.NoRoleException'}">
			                                                                                                   请联系管理员为该用户指定角色
		                                </c:when>
	                            </c:choose>
                             		
                             </div>
                       		 <div class="logo-sub"><input type="button" value=""  onclick="onLoginClick()" /></div>
                       </div>
                       </form>
                      
                    </div>
                </div>
            	
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript" src="${js}/jquery.cookie.min.js"></script>
<script type="text/javascript">

    var b = false;
    var p = window;
    if (p != p.parent) {
        b = true;
    }
    ;
    while (b) {
        if (p == p.parent) {
            p.location.href = "${ctx}/login";
            b = false;
        } else {
            p = p.parent;
        }
        ;
    }
    ;

    $("#username").val($.cookie('admin_user_name'));
    $("#password").val('');

    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            onLoginClick();
        }
    });

    function onLoginClick() {
        if (!$("#username").val()) {
            $('#error').text('用户名不能为空');
            return;
        }
        if (!$("#password").val()) {
            $('#error').text('密码不能为空');
            return;
        }
        if ($("#rememberUserName").prop('checked')) {
            $.cookie('admin_user_name', $("#username").val(), {expires: 7});
        } else {
            $.removeCookie('admin_user_name');
        }
        $("#dialogForm").submit();
    }

    function onResetClick() {
       $("#username").val('');
        $("#password").val('');
    }
</script>
</body>
</html>
