<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>
<!doctype html>
<html>
<head>
    <title></title>
    <jsp:include page="/meta.jsp"/>
    <style type="text/css">
        html, body {
            margin: 0;
            padding: 0;
            border: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }
    </style>
</head>
<body>
<div style="padding-bottom:5px;">
    <span>开始时间:</span>
	        <input id="startDate" class="mini-datepicker" style="width:150px;" nullValue="null"
      			  format="yyyy-MM-dd" showTime="false" showOkButton="true" showClearButton="true"/>
	        <span >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	         <span>结束时间:</span>
	        <input id="endDate" class="mini-datepicker" style="width:150px;" nullValue="null"
       			 format="yyyy-MM-dd" showTime="false" showOkButton="true" showClearButton="true"/>
	        <span >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	        <span>真实姓名:</span><input class="mini-textbox" id="search_like_userName"/>
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a class="mini-button" iconCls="icon-edit" onclick="editPrivate()">授予权限</a>
                <a class="mini-button" iconCls="icon-edit" onclick="editPrivate2()">解除权限</a>
            </td>
        </tr>
    </table>
</div>

<div class="mini-fit">
    <div id="logDategrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
         url="${ctx}/device/authority/list" idField="user_id" multiSelect="true">
        <div property="columns">
            <div field="device_guid" width="120" headerAlign="center" allowSort="true">设备号</div>
            <div field="remarks" width="120" headerAlign="center" allowSort="true">设备名称</div>
            <div field="user_name" width="120" headerAlign="center" allowSort="true">用户名</div>
            <div field="device_authority" width="120" headerAlign="center" renderer="onStatusRenderer"  allowSort="true">是否有操作设备权限</div>
            </div>
        </div>
    </div>


<script type="text/javascript">
    mini.parse();

    var grid = mini.get("logDategrid");
    grid.load();

    //搜索
    function search() {
        var startDate = mini.get("startDate").getFormValue();
        var endDate = mini.get("endDate").getFormValue();
        var user_name = mini.get("search_like_userName").getValue();
        grid.load({
        	startDate: startDate,
        	endDate: endDate,
        	user_name : user_name
        });
    }

    //清空
    function reset() {
    	mini.get("startDate").setValue("");
    	mini.get("endDate").setValue("");
    	mini.get("search_like_userName").setValue("");
        grid.reload();
    }

    //////////// /////////////////////////////////////
    function onStatusRenderer(e) {
        <c:forEach var="status" items='${AUTHORITY}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }

  
    function editPrivate(){
    	var row = grid.getSelected();
        if (row) {
        	if(row.device_authority == '0'){
        		var url = "${ctx}/device/authority/editPrivate/";
                $.post(url, {'device_authority': 1,"user_id":row.user_id,"device_id":row.device_id}, function (data) {
                    if (data) {
                        notify("操作成功");
                    } else {
                        notify("操作失败");
                    }
                    grid.reload();
                });
        	}else{
           	  mini.alert("该用户已经有该设备的操作权限", "提示", function callback(action){return;});
        	}
        }else{
         	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }
    	
    }
    
    function editPrivate2(){
    	var row = grid.getSelected();
        if (row) {
        	if(row.device_authority == '1'){
        		var url = "${ctx}/device/authority/editPrivate/";
                $.post(url, {"device_authority": 0,"user_id":row.user_id,"device_id":row.device_id}, function (data) {
                    if (data) {
                        notify("操作成功");
                    } else {
                        notify("操作失败");
                    }
                    grid.reload();
                });
        	}else{
           	  mini.alert("该用户已经没有此设备的操作权限", "提示", function callback(action){return;});
        	}
        }else{
         	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }
    	
    }
    
</script>
</body>
</html>