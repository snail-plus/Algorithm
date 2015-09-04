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
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a class="mini-button" iconCls="icon-add" onclick="edit('f_insert')">增加</a>
                <a class="mini-button" iconCls="icon-edit" onclick="edit('f_edit')">编辑</a>
                <a class="mini-button" iconCls="icon-remove" onclick="remove()">删除</a>
            </td>
        </tr>
    </table>
</div>
<div class="mini-fit">
    <div id="optTreeGrid" class="mini-treegrid" style="width:100%;height:100%;" url="${ctx}/rbac/funopt/listAll"
         showTreeIcon="true" treeColumn="fun_opt_nm" idField="fun_opt_id" parentField="parent_fun_opt_id"
         resultAsTree="false" allowResize="true" expandOnLoad="true">
        <div property="columns">
            <div type="indexcolumn"></div>
            <div name="fun_opt_nm" field="fun_opt_nm" width="160">URL</div>
            <div field="url" width="240">URL前缀</div>
            <div field="remarks" width="300" >备注</div>
        </div>
    </div>
</div>
<div id="editWindow" class="mini-window" title="编辑" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="editform" class="form">
        <input class="mini-hidden" name="fun_opt_id"/>
        <input class="mini-hidden" name="parent_fun_opt_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">URL:</td>
                <td style="width:150px;">
                    <input name="fun_opt_nm" class="mini-textbox" onvalidation="onNmInValidation" required="true" vtype="maxLength:32"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">URL前缀:</td>
                <td style="width:150px;">
                    <input name="url" class="mini-textbox" onvalidation="onUrlInValidation" required="true" vtype="maxLength:64"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;"><input name="remarks" class="mini-textarea" vtype="maxLength:64"/></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveOpt('f_edit')" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="addWindow" class="mini-window" title="增加" style="width:350px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="addform" class="form">
        <input class="mini-hidden" name="fun_opt_id"/>
        <input class="mini-hidden" name="parent_fun_opt_id"/>
        <table style="width:100%;">
            <!-- <tr>
                <td style="width:80px;">父节点：</td>
                <td style="width:150px;">
                    <input name="roleNm" class="mini-textbox"  allowInput="false" />
                </td>
            </tr> -->
            <tr>
                <td style="width:80px;">URL:</td>
                <td style="width:150px;">
                    <input name="fun_opt_nm" class="mini-textbox" onvalidation="onNmValidation" required="true" vtype="maxLength:32"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">URL前缀:</td>
                <td style="width:150px;">
                    <input name="url" class="mini-textbox" onvalidation="onUrlValidation" required="true" vtype="maxLength:64"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;"><input name="remarks" class="mini-textarea" vtype="maxLength:64"/></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveOpt('f_insert')" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
mini.parse();
var optTreeGrid = mini.get("optTreeGrid");
optTreeGrid.load();
optTreeGrid.sortBy("fun_opt_id", "asc");
var addWindow = mini.get("addWindow");
var editWindow = mini.get("editWindow");

function edit(flag) {
    if ("f_insert" == flag) {
        var form = new mini.Form("#addform");
        form.reset();
        var row = optTreeGrid.getSelected();
        if (row) {
            if (typeof(row.parent_fun_opt_id) == "undefined") {
                addWindow.show();
            } else {
           	 	 mini.alert("不能在该节点下添加子节点", "提示", function callback(action){return;});
            }
        } else {
            addWindow.show();
        }
    } else if ("f_edit" == flag) {
        var row = optTreeGrid.getSelected();
        if (row) {
            editWindow.show();
            var form = new mini.Form("#editform");
            form.clear();
            form.loading();
            $.ajax({
                url: '${ctx}/rbac/funopt/getFunopt/' + row.fun_opt_id,
                success: function (text) {
                    form.unmask();
                    var o = mini.decode(text);
                    form.setData(o);
                },
                error: function () {
                    notify("表单加载错误");
                    form.unmask();
                }
            });
        } else {
			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
        }
    }

}
function cancel() {
    //optTreeGrid.reload();
    editWindow.hide();
    addWindow.hide();
}
function remove() {
    var row = optTreeGrid.getSelected();
    if (row) {
        //删除之前验证所选记录是否存在
        $.post('${ctx}/rbac/funopt/delFreCheck/' + row.fun_opt_id, function (msg1) {
            if (msg1==false) {
            	//验证是否存在子节点
                $.post('${ctx}/rbac/funopt/checkChild/' + row.fun_opt_id, function (msg2) {
                    if (msg2==false) {
         			   mini.alert("不能删除有子节点的父节点.", "提示", function callback(action){return;});
               		 }else{
               			 //验证是否绑定有角色
               			  $.post('${ctx}/rbac/funopt/checkBindRole/' + row.fun_opt_id, function (msg3) {
               				  if(msg3==false){               					  
                    			   mini.alert("不能删除已经绑定角色的资源", "提示", function callback(action){return;});
               				  }else{
               					  //验证是否绑定菜单
               					  $.post('${ctx}/rbac/funopt/checkBindMenu/' + row.fun_opt_id, function (msg4) {
               						 if(msg4==false){               					  
                          			   mini.alert("不能删除已经绑定菜单的资源", "提示", function callback(action){return;});
               						 }else{
               							 mini.confirm("您确定要删除此记录吗?", "提示", function (msg5) {
                                             if (msg5 == 'ok') {
                                                 $.post('${ctx}/rbac/funopt/delete/' + row.fun_opt_id, function (data) {
                                                     if (data) {
                                                         notify('删除成功!');
                                                         optTreeGrid.reload();
                                                     } else {
                                                         notify('删除失败!');
                                                     }
                                                 });
                                             }
                                         });
               							 
               						 }
               						  
               					  });
               				}
               				  
               			  });
               			 }
                    
       			 });
            }else{
   			   mini.alert("未找到该资源,请刷新后再试", "提示", function callback(action){return;});
            }
       });
    } else {
		   mini.alert("请选择一条记录.", "提示", function callback(action){return;});
    }
}
function saveOpt(flag) {
    var url = "";
    var form;
    if ("f_insert" == flag) {
        form = new mini.Form("#addform");
        var node = optTreeGrid.getSelectedNode();
        if (node) {
            url = "${ctx}/rbac/funopt/createByParam/" + node.fun_opt_id;
        } else {
            url = "${ctx}/rbac/funopt/create";
        }
    } else if ("f_edit" == flag) {
        form = new mini.Form("#editform");
        url = "${ctx}/rbac/funopt/update";
    }
    form.validate();
    if (form.isValid() == true) {
        var o = form.getData();
        o.fun_opt_nm=o.fun_opt_nm.trim();
        o.url=o.url.trim();
        $.post(url, o, function (msg) {
            if (msg) {
                if ("f_insert" == flag) {
                    notify("添加成功");
                } else if ("f_edit" == flag) {
                	 notify("编辑成功");
                }
                optTreeGrid.reload();
            } else {
                notify("服务器繁忙，请稍后重试");
                grid.reload();
            }
        });
        editWindow.hide();
        addWindow.hide();
    }
}

//////////// /////////////////////////////////////

function onNmInValidation(e) {
    if (e.isValid) {
        var row = optTreeGrid.getSelected();
        var funOptNm = row.fun_opt_nm.trim();
        if (funOptNm != e.value.trim()) {
            $.ajax({
                url: '${ctx}/rbac/funopt/check',
                async: false,
                data: {funOptNm: e.value},
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
function onNmValidation(e) {
    if (e.isValid) {
        $.ajax({
            url: '${ctx}/rbac/funopt/check',
            async: false,
            data: {funOptNm: e.value.trim()},
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
function onUrlInValidation(e) {
    if (e.isValid) {
        var row = optTreeGrid.getSelected();
        var url = row.url.trim();
        if (url != e.value.trim()) {
            $.ajax({
                url: '${ctx}/rbac/funopt/checkUrl',
                async: false,
                data: {url: e.value.trim()},
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
function onUrlValidation(e) {
    if (e.isValid) {
        $.ajax({
            url: '${ctx}/rbac/funopt/checkUrl',
            async: false,
            data: {url: e.value.trim()},
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