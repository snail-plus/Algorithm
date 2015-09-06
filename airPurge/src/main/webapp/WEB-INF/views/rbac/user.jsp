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
    <span>登录名称:</span><input type="text" id="search_like_loginId"/>
    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <span>真实姓名:</span><input type="text" id="search_like_userName"/>
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                 <a class="mini-button" iconCls="icon-add" onclick="add()">增加</a>
                <a class="mini-button" iconCls="icon-edit" onclick="edit()">编辑</a>
                <a class="mini-button" iconCls="icon-role" onclick="bandRoleInfo()">绑定角色</a>
            </td>
        </tr>
    </table>
</div>
<div class="mini-fit">
    <div id="userDategrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
         url="${ctx}/rbac/user/list" idField="user_id" multiSelect="true">
         <div property="columns">
            <div field="user_id" width="30" headerAlign="center" allowSort="true">用户编号</div>
            <div field="login_id" width="120" headerAlign="center" allowSort="true">登录名称</div>
            <div field="user_name" width="120" headerAlign="center" allowSort="true">真实姓名</div>
            <div field="user_type" width="120" renderer="onTypeRenderer" headerAlign="center" allowSort="true">用户类型</div>
            <div field="user_status" width="120" renderer="onStatusRenderer" headerAlign="center" allowSort="true">是否有效
            </div>
           <!--  <div field="device_authority" width="120" renderer="onStatusRenderer2" headerAlign="center" allowSort="true">是否有操作权限
            </div> -->
        </div>
    </div>
</div>
<div id="addWindow" class="mini-window" title="增加" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="addform" class="form">
        <input class="mini-hidden" name="user_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">登录名称:</td>
                <td style="width:150px;">
                    <input name="login_id" id="login_id" class="mini-textbox" required="true"  onvalidation="onLoginIdValidation" vtype="maxLength:64"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">真实姓名:</td>
                <td style="width:150px;"><input name="user_name" class="mini-textbox" required="true" vtype="maxLength:32"/></td>
            </tr>
            <tr>
                <td style="width:80px;">密码:</td>
                <td style="width:150px;"><input id="pwds" onvalidation="onPwdsValidation" class="mini-password"
                                                required="true" vtype="maxLength:64"/></td>
            </tr>
            <tr>
                <td style="width:80px;">确认密码:</td>
                <td style="width:150px;"><input name="pwd" onvalidation="onPwdValidation" class="mini-password"
                                                required="true"/></td>
            </tr>
            <tr>
                <td style="width:80px;">是否有效:</td>
                <td style="width:150px;"><input name="user_status"  textField="text" required="true" url="${ctx}/base/dict/get/COMM_TYPE"
                       valueField="id" allowInput="false" class="mini-combobox" /></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="insertUser()" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="editWindow" class="mini-window" title="编辑" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="editform" class="form">
        <input class="mini-hidden" name="user_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">真实姓名:</td>
                <td style="width:150px;"><input name="user_name" class="mini-textbox" required="true" vtype="maxLength:64"/></td>
            </tr>
            <tr>
                <td style="width:80px;">是否有效:</td>
                <td style="width:150px;"><input name="user_status"  textField="text" url="${ctx}/base/dict/get/COMM_TYPE"
                       valueField="id" allowInput="false" class="mini-combobox"/>
                </td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="updateUser()" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="roleBindWindow" class="mini-window" title="角色管理" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="listRole" class="mini-listbox" style="width:340px;height:120px;" showCheckBox="true" multiSelect="true">
        <div property="columns">
            <div header="角色" field="text"></div>
        </div>
    </div>
    <div style="text-align: center; padding: 10px;">
        <a class="mini-button" onclick="doBandRoleInfo()" style="width: 60px; margin-right: 20px;">确定</a>
        <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
    </div>
</div>


<script type="text/javascript">
    mini.parse();

    var grid = mini.get("userDategrid");
    grid.load();
    grid.sortBy("user_id", "asc");
    var editWindow = mini.get("editWindow");
    var addWindow = mini.get("addWindow");

    var roleBindWindow = mini.get("roleBindWindow");

    var listUser = mini.get("listUser");
    var listRole = mini.get("listRole");

    function add() {
        var form = new mini.Form("addform");
        form.clear();
        addWindow.show();
    }

    function edit() {
        var row = grid.getSelected();
        if (row) {
            editWindow.show();
            var form = new mini.Form("#editform");
            form.clear();
            form.loading();
            $.ajax({
                url: '${ctx}/rbac/user/' + row.user_id,
                success: function (text) {
                    form.unmask();
                    var o = mini.decode(text);
                    form.setData(o);
                },
                error: function () {
              	  mini.alert("表单加载错误", "提示", function callback(action){return;});
                    form.unmask();
                }
            });
        } else {
        	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }

    }

    //搜索
    function search() {
        var search_like_loginId = $("#search_like_loginId").val();
        var search_like_userName = $("#search_like_userName").val();
        grid.load({
            login_id: search_like_loginId,
            user_name: search_like_userName
        });
    }

    //清空
    function reset() {
        $("#search_like_loginId").val("");
        $("#search_like_userName").val("");
        grid.reload();
    }

    function cancel() {
        //grid.reload();
        addWindow.hide();
        editWindow.hide();
        roleBindWindow.hide();
    }

    function updateUser() {
        var form = new mini.Form("#editform");
        form.validate();
        if (form.isValid() == true) {
            var o = form.getData();
            $.post("${ctx}/rbac/user/update", o, function (msg) {
                if (msg) {
                	notify("编辑成功");
                    grid.reload();
                } else {
                	notify("服务器繁忙，请稍后重试");
                }
            });
            editWindow.hide();
        }
    }
    
    function insertUser() {
    	 var login_id = mini.get("login_id").getValue().trim();
    	$.post("${ctx}/rbac/user/checkLoginId",{'loginId': login_id} , function (response) {
           if (!response) {
         	  mini.alert("已经存在的用户名", "提示", function callback(action){return;});
           }else{
        	   var form = new mini.Form("addform");
               form.validate();
               var o = form.getData();
               if (form.isValid() == true) {
                   $.post("${ctx}/rbac/user/create", o, function (msg) {
                       if (msg) {
                       	 notify("添加成功");
                           grid.reload();
                       } else {
                       	notify("服务器繁忙，请稍后重试");
                           grid.reload();
                       }
                   });
                   addWindow.hide();
               }   
           }
	    });
    }

    function bandRoleInfo() {
        var row = grid.getSelected();
        if (row) {
        	 if(row.user_type=='2'){
             	  mini.alert("终端用户没有此操作", "提示", function callback(action){return;});
            }else{
            	$.ajax({
                    url: "${ctx}/rbac/user/getAllRole/" + row.user_id,
                    dataType: "json",
                    type: "get",
                    contentType: 'application/json;charset=UTF-8',
                    success: function (text) {
                        var myobj = eval(text);
                        var str = "";
                        for (var i = 0; i < myobj.length; i++) {
                            if (myobj[i].ischecked) {
                                str += myobj[i].id + ",";
                            }
                        }
                        listRole.loadData(myobj);
                        listRole.setValue(str);
                        roleBindWindow.show();
                    },
                    error: function () {
                    	 notify("表单加载错误");
                        form.unmask();
                    }
                });
            }
            
        } else {
       	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }
    }

    function doBandRoleInfo() {
        var row = grid.getSelected();
        var value = listRole.getValue();
        var url = "${ctx}/rbac/user/insertUesrRole/";
        $.post(url + row.user_id, {'boundInfos': value}, function (data) {
            if (data) {
                notify("绑定成功");
                roleBindWindow.hide();
            } else {
                roleBindWindow.hide();
                notify("绑定失败");
            }
        });
    }
    //////////// /////////////////////////////////////
    function onStatusRenderer(e) {
        <c:forEach var="status" items='${commTypeList}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }
    
    function onStatusRenderer2(e) {
        <c:forEach var="status" items='${AUTHORITY}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }

    function editPrivate(){
    	var row = grid.getSelected();
        if (row) {
        	if(row.user_type== '2'){
        		var url = "${ctx}/rbac/user/editPrivate/";
                $.post(url, {'device_authority': row.device_authority,"user_id":row.user_id}, function (data) {
                    if (data) {
                        notify("操作成功");
                    } else {
                        notify("操作成功");
                    }
                });
        	}else{
           	  mini.alert("非终端用户没有此操作", "提示", function callback(action){return;});

        	}
        }else{
         	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }
    	
    }
    
    function onStatusRenderer(e) {
        <c:forEach var="status" items='${commTypeList}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }
    
    function onTypeRenderer(e) {
        <c:forEach var="type" items='${userType}'>
       	 if('${type.id}'==e.value){
       		 return '${type.text}';
       	   }
		</c:forEach>
    }

    function onPwdValidation(e) {
        if (e.isValid) {
            var pwds = mini.get("pwds").getValue();
            if (e.value != pwds) {
            	 e.errorText = "请再次输入相同的值";
                e.isValid = false;
            }
        }
    }

    function onPwdsValidation(e) {
        if (e.isValid) {
            if (e.value.length < 5) {
            	 e.errorText = "字段长度不能小于5";
                 e.isValid = false;
            }
            
            if((e.value.charAt(0)==" ")||(e.value.charAt(e.value.length-1)==" ")){
          		 e.errorText = "密码前后不能有空格";
                e.isValid = false;
          	}
            
        }
    }
    
    function onLoginIdValidation(e) {
        if (e.isValid) {
           mini.get("login_id").setValue(e.value.trim());
     	   $.post("${ctx}/rbac/user/checkLoginId",{'loginId': e.value.trim()} , function (response) {
	           if (!response) {
	         	  mini.alert("已经存在的用户名", "提示", function callback(action){return;});
	         	  e.isValid = false;
	           }
         });
     	   
        }
    }

</script>
</body>
</html>