<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="res" value="${ctx}/static" />
<c:set var="css" value="${res}/css"/>
<c:set var="img" value="${res}/images"/> 
<script src="${res}/js/boot.js" type="text/javascript"></script>
<script src="${res}/js/crack.js" type="text/javascript"></script>

<link rel="stylesheet" href="${css}/style.css" type="text/css">  
<script type="text/javascript">

$.namespace = function() {
    var a=arguments, o=null, i, j, d;
    console.info("a: " + JSON.stringify(a));
    console.info("d: " + JSON.stringify(d));
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=window;
        for (j=0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }
    return o;
};

function notify(message) {

	
    var x = document.getElementById("x").value;
    var y = document.getElementById("y").value;
    mini.showMessageBox({
        showModal: false,
        width: 250,
        title: "提示",
        //iconCls: "mini-messagebox-warning",
        message: message,
        timeout: 4000,
        x: x,
        y: y
    });
}

function checkNull(obj){
	if (!obj && typeof(obj)!="undefined" && obj!=0)
	{
	    return true;
	}else if(obj == ""){
		return true;
	}else{
		return false;
	}
}

function validMobil(value){
	var reg = /^1[3|4|5|8][0-9]\d{4,8}$/;
	if(reg.test(value))return true;
	return false;
}

$(function() {
	$('div > .mini-button').click(function() {
		var $btn = mini.get(this);
		$btn.setEnabled(false);
		setTimeout(function() {
			$btn.setEnabled(true);
		}, 1000);
	});
	
	/* MessageBox
	-----------------------------------------------------------------------------*/
	if (mini.MessageBox) {
	    mini.copyTo(mini.MessageBox, {
	        alertTitle: "Alert",
	        confirmTitle: "Confirm",
	        prompTitle: "Prompt",
	        prompMessage: "Input content: ",
	        buttonText: {
	            ok: "确定", //"OK",
	            cancel: "取消", //"Cancel",
	            yes: "是", //"Yes",
	            no: "否"//"No"
	        }
	    });
	};
});

/* Calendar
-----------------------------------------------------------------------------*/
if (mini.Calendar) {
    mini.copyTo(mini.Calendar.prototype, {
        firstDayOfWeek: 0,
        todayText: "今天",
        clearText: "清除",
        okText: "确定",
        cancelText: "取消",
        daysShort: ["日", "一", "二", "三", "四", "五", "六"],
        format: "yyyy年MM月",

        timeFormat: 'H:mm'
    });
}



/**
 * 设置未来(全局)的AJAX请求默认选项
 * 主要设置了AJAX请求遇到Session过期的情况
 */
 $.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",     
    complete:function(XMLHttpRequest,textStatus){  
    	// 通过XMLHttpRequest取得响应头，sessionstatus，  
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
        if(sessionstatus=="sessionOut"){ 
        	 var top = getTopWinow();
             mini.confirm("由于您长时间没有操作, 身份已过期, 请重新登录后操作.", "提示", function (action) {
            	 if (action == "ok") {
            		 logout();
                 } 
             });
        }
    }
});
/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * @return 当前页面的顶层窗口对象
 */
function getTopWinow(){
    var p = window;
    while(p != p.parent){
        p = p.parent;
    }
    return p;
}

//注销用户
function logout() {
    var url = '${ctx}/logout';
    window.parent.location = url;
}
</script>
<select id="x" style="display: none">
	<option value="right" selected>Right</option>
</select>
<select id="y"  style="display: none">
	<option value="bottom" selected>Bottom</option>
</select>