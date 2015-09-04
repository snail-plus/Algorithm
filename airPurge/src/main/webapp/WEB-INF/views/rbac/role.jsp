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
    <span>角色</span><input class="mini-textbox" id="roleName"/>
    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <span>备注</span><input class="mini-textbox" id="roleDesc"/>
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a class="mini-button" iconCls="icon-add" onclick="edit('f_insert')">增加</a>
                <a class="mini-button" iconCls="icon-edit" onclick="edit('f_edit')">编辑</a>
                <a class="mini-button" iconCls="icon-remove" onclick="remove()">删除</a>
                <a class="mini-button" iconCls="icon-resource" onclick="bandResource()">绑定资源</a>
            </td>
        </tr>
    </table>
</div>
<div class="mini-fit">
    <div id="roleDategrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
         url="${ctx}/rbac/role/listAll" idField="role_id" multiSelect="true">
        <div property="columns">
            <div field="role_id" width="30" headerAlign="center" allowSort="true">角色编号</div>
            <div field="role_nm" width="120" headerAlign="center" allowSort="true">角色</div>
            <div field="role_desc" width="120" headerAlign="center" allowSort="true">备注</div>
        </div>
    </div>
</div>
<div id="editWindow" class="mini-window" title="编辑" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="editform" class="form">
        <input class="mini-hidden" name="role_id" id="role_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">角色:</td>
                <td style="width:150px;">
                    <input name="role_nm" class="mini-textbox" onvalidation="onRoleNmValidation" required="true" vtype="maxLength:32" />
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;"><input name="role_desc" class="mini-textarea" vtype="maxLength:64"/></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveRole('f_edit')" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="addWindow" class="mini-window" title="增加" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="addform" class="form">
        <input class="mini-hidden" name="role_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">角色:</td>
                <td style="width:150px;">
                    <input name="role_nm" class="mini-textbox" onvalidation="onRoleNmInValidation" required="true" vtype="maxLength:32"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;"><input name="role_desc" class="mini-textarea" vtype="maxLength:64"/></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveRole('f_insert')" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="resourceWindow" class="mini-window" title="绑定资源" style="width:380px;height: 420px;" showModal="true"
     allowResize="true" allowDrag="true">
    <div class="mini-fit">
        <ul id="resourceTree" class="mini-tree" style="width:200px;padding:5px;"
            showTreeIcon="true" textField="text" idField="id" parentField="pid" resultAsTree="false"
            showCheckBox="true" checkRecursive="true" allowSelect="false" enableHotTrack="false" expandOnLoad="true">
        </ul>
    </div>
    <div style="text-align: center; padding: 10px;">
        <a class="mini-button" onclick="doBandResource()" style="width: 60px; margin-right: 20px;">确定</a>
        <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
    </div>
</div>

<script type="text/javascript">

mini.parse();
var grid = mini.get("roleDategrid");
grid.load();
grid.sortBy("role_id", "asc");

var addWindow = mini.get("addWindow");
var editWindow = mini.get("editWindow");
var resourceWindow = mini.get("resourceWindow");
var resourceTree = mini.get("resourceTree");

function edit(flag) {
    if ("f_insert" == flag) {
        var form = new mini.Form("#addform");
        form.reset();
        addWindow.show();
    } else if ("f_edit" == flag) {
        var row = grid.getSelected();
        if (row) {
            editWindow.show();
            var form = new mini.Form("#editform");
            form.clear();
            form.loading();
            $.ajax({
                url: '${ctx}/rbac/role/' + row.role_id,
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
	    	  mini.alert("请先选择一条记录", "提示", function callback(action){return;});
        }
    }
}

//搜索
function search() {
    var roleName = mini.get("roleName").getFormValue();
    var roleDesc = mini.get("roleDesc").getFormValue();
    grid.load({
        'role_nm': roleName,
        'role_desc': roleDesc
    });
}

//清空
function reset() {
    mini.get("roleName").setValue('');
    mini.get("roleDesc").setValue('');
    grid.reload();
}

//取消
function cancel() {
    //grid.reload();
    editWindow.hide();
    addWindow.hide();
    resourceWindow.hide();
}
function remove() {
    var row = grid.getSelected();
    if (row) {
        //删除之前验证所选记录是否存在
        $.post('${ctx}/rbac/role/delCheckExits/' + row.role_id, function (checkmsg1) {
            if (checkmsg1==false) {
            	
            	  $.post('${ctx}/rbac/role/checkUserBind/' + row.role_id, function (checkmsg2) {
            		  if (checkmsg2==true) {
                          mini.confirm("您确定要删除此记录吗?", "提示", function (action) {
                              if (action == 'ok') {
                                          $.post('${ctx}/rbac/role/delete/' + row.role_id, function (data) {
                                              if (data) {
                                                  notify('删除成功!');
                                                  grid.reload();
                                              } else {
                                                  notify('删除失败!');
                                              }
                                          });
                                      } 
                          });
            		  }else{
                   	 	 mini.alert("不能删除已经绑定用户的角色.", "提示", function callback(action){return;});
            			  
            		  }
            		  
            	  });
                
            } else {
          	 	 mini.alert("角色不存在，请刷新后重试", "提示", function callback(action){return;});
            }
        });
    } else {
  	  mini.alert("请先选择一条记录.", "提示", function callback(action){return;});
    }
}

function saveRole(flag) {
    var url = "";
    var form;
    if ("f_insert" == flag) {
        url = "${ctx}/rbac/role/create";
        form = new mini.Form("#addform");
    } else if ("f_edit" == flag) {
        url = "${ctx}/rbac/role/update";
        form = new mini.Form("#editform");
    }
    form.validate();
    if (form.isValid() == true) {
        var o = form.getData();
        $.post(url, o, function (msg) {
            if (msg) {
                if ("f_insert" == flag) {
                    notify("添加成功");
                } else if ("f_edit" == flag) {
                	notify("编辑成功");
                }
                grid.reload();
            } else {
                notify("服务器繁忙，请稍后重试");
                grid.reload();
            }
            editWindow.hide();
            addWindow.hide();
        });
    }
}

function bandResource() {
    var row = grid.getSelected();
    if (row) {
        $.ajax({
            url: '${ctx}/rbac/role/allOpt/' + row.role_id,
            dataType: "json",
            type: "get",
            contentType: 'application/json;charset=UTF-8',
            success: function (text) {
                var myobj = eval(text);
                resourceTree.loadData(myobj);
                resourceWindow.show();
            },
            error: function () {
                notify("表单加载错误");
            }
        });
    } else {
  	  mini.alert("请先选择一条记录", "提示", function callback(action){return;});
    }
}

function doBandResource() {
    var row = grid.getSelected();
    var value = resourceTree.getValue(false);
    if (value.length > 0) {
        $.post(
                '${ctx}/rbac/role/customer_bund/'
                + row.role_id + "/"
                + value,
                function (data) {
                    if (data) {
                        notify('绑定成功!');
                    } else {
                        notify('绑定失败!');
                    }
				    resourceWindow.hide();
                    grid.reload();
                });
    } else {
    	  mini.alert("还没有选择任何资源", "提示", function callback(action){return;});
    }
}
//////////// /////////////////////////////////////

function onRoleNmValidation(e) {
    if (e.isValid) {
        var row = grid.getSelected();
        var roleNm = row.role_nm;
        if (roleNm != e.value) {
            $.ajax({
                url: '${ctx}/rbac/role/checkRoleNm',
                async: false,
                data: {role_nm: e.value},
                success: function (text) {
                    if (!text) {
                        e.errorText = "该名称已经存在!";
                        e.isValid = false;
                    }
                },
                error: function () {
                    notify("表单加载错误");
                    // e.errorText = "密码不能少于5个字符";
                    e.isValid = false;
                }
            });
        }

    }
}
function onRoleNmInValidation(e) {
    if (e.isValid) {
        $.ajax({
            url: '${ctx}/rbac/role/checkRoleNm',
            async: false,
            data: {role_nm: e.value},
            success: function (text) {
                if (!text) {
                    e.errorText = "该名称已经存在!";
                    e.isValid = false;
                }
            },
            error: function () {
                notify("表单加载错误");
              
                e.isValid = false;
            }
        });
    }
}


</script>
</body>
</html>