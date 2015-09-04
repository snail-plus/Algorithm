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
                <a class="mini-button" iconCls="icon-upload" onclick="move('up')">上移</a>
                <a class="mini-button" iconCls="icon-download" onclick="move('down')">下移</a>
            </td>
        </tr>
    </table>
</div>
<div class="mini-fit">
    <div id="optTreeGrid" class="mini-treegrid" style="width:100%;height:100%;" url="${ctx}/rbac/menue/listAll"
         showTreeIcon="true" treeColumn="menue_nm" idField="menue_id" parentField="par_menue_id" resultAsTree="false"
         allowResize="true" expandOnLoad="true">
        <div property="columns">
            <div type="indexcolumn"></div>
            <div name="menue_nm" field="menue_nm" width="260">菜单名称</div>
            <div field="remarks" width="240">备注</div>
            <div field="state" renderer="onStatusRenderer" width="100" align="left">是否有效</div>
        </div>
    </div>
</div>
<div id="editWindow" class="mini-window" title="编辑" style="width:380px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="editform" class="form">
        <input class="mini-hidden" name="menue_id"/>
        <input class="mini-hidden" name="par_menue_id"/>
        <input class="mini-hidden" name="menue_order"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">菜单名称:</td>
                <td style="width:150px;">
                    <input name="menue_nm" style="width: 150px;" class="mini-textbox" onvalidation="onNmInValidation"
                           required="true" vtype="maxLength:32"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">URL:</td>
                <td style="width:150px;">
                    <input name="fun_opt_id" style="width:150px;" class="mini-treeselect" id="optTreeSelect"
                           multiSelect="false" valueFromSelect="false" required="true" expandOnLoad="true"
                           textField="fun_opt_nm" valueField="fun_opt_id" parentField="parent_fun_opt_id"
                           allowInput="false" onbeforenodeselect="onEditBeforeNodeSelect"
                           showRadioButton="true" showFolderCheckBox="false" 
                            />
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;">
                    <input name="remarks" style="width: 150px;" class="mini-textArea" vtype="maxLength:64" />
                </td>
            </tr>
                <tr>
	                <td style="width:80px;">是否有效：</td>
	                <td style="width:200px;"><input name="state" url="${ctx}/base/dict/get/COMM_TYPE" class="mini-combobox" required="true"/></td>
	         </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveOpt('f_edit')" style="width: 60px; margin-right: 20px;">确定</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>
<div id="addWindow" class="mini-window" title="增加" style="width:380px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="addform" class="form">
        <input class="mini-hidden" name="menue_id"/>
        <input class="mini-hidden" name="par_menue_id"/>
        <input class="mini-hidden" name="menue_order"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">菜单名称:</td>
                <td style="width:150px;">
                    <input name="menue_nm" class="mini-textbox" onvalidation="onNmValidation" style="width: 150px;"
                           required="true" vtype="maxLength:32" />
                </td>
            </tr>
            <tr>
                <td style="width:80px;">URL:</td>
                <td style="width:150px;">
                    <input id="a_fun_opt" name="fun_opt_id" style="width:150px;" class="mini-treeselect"
                            expandOnLoad="true"
                           multiSelect="false" valueFromSelect="false" required="true"
                           textField="fun_opt_nm" valueField="fun_opt_id" parentField="parent_fun_opt_id"
                           allowInput="false" onbeforenodeselect="onInsertBeforeNodeSelect"
                           showRadioButton="true" showFolderCheckBox="false"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:180px;">
                    <input name="remarks" class="mini-textArea" style="width: 150px;" vtype="maxLength:64"/>
                </td>
            </tr>
             <tr>
	                <td style="width:80px;">是否有效：</td>
	                <td style="width:200px;"><input name="state" url="${ctx}/base/dict/get/COMM_TYPE" class="mini-combobox" required="true"/></td>
	         </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveOpt('f_insert')" style="width: 60px; margin-right: 20px;">确认</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
    mini.parse();
    var optTreeGrid = mini.get("optTreeGrid");
   
    var addWindow = mini.get("addWindow");
    var editWindow = mini.get("editWindow");

    var optTreeSelect=mini.get("optTreeSelect");
    
    function edit(flag) {
        if ("f_insert" == flag) {
            var form = new mini.Form("#addform");
            form.reset();
            var row = optTreeGrid.getSelected();
            if (row) {
		           	var parent_fun_opt_id=row.fun_opt_id;
		           	url="${ctx}/rbac/funopt/getAllNotBind/"+parent_fun_opt_id;
		           	var a_fun_opt=mini.get("a_fun_opt");
		           	a_fun_opt.setUrl(url);
                if (row.par_menue_id == 1) {
                    addWindow.show();
                } else {
               	  mini.alert("不能在该节点下添加子节点", "提示", function callback(action){return;});
                }
            } else {
            	url="${ctx}/rbac/funopt/getAllNotBind/"+0;
	           	var a_fun_opt=mini.get("a_fun_opt");
	           	a_fun_opt.setUrl(url);
                addWindow.show();
            }

        } else if ("f_edit" == flag) {
        	
            var row = optTreeGrid.getSelected();
            if (row) {
            	optTreeSelect.load('${ctx}/rbac/funopt/editOptTreeSelect/' + row.fun_opt_id);
                editWindow.show();
                var form = new mini.Form("#editform");
                form.clear();
                form.loading();
                $.ajax({
                    url: '${ctx}/rbac/menue/getMenueById/' + row.menue_id,
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
     		   mini.alert("请选择一条记录.", "提示", function callback(action){return;});
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
        	//判断菜单是否有效
        	if(row.state=='1'){
      		   mini.alert("不能删除有效的菜单.", "提示", function callback(action){return;});
        	}else{
            //删除之前验证所选记录是否存在
            $.post("${ctx}/rbac/menue/fredelcheck/" + row.menue_id, function (msg) {
                if (msg) {
                    $.post("${ctx}/rbac/menue/checkChild/" + row.menue_id, function (checkMsg) {
                        if (checkMsg) {
                            mini.confirm("确定删除记录吗?", "提示", function (action) {
                                if (action == 'ok') {
                                    //删除选中记录
                                    $.post("${ctx}/rbac/menue/deleteMenue/" + row.menue_id, function (result) {
                                        if (result) {
                                            notify('删除成功.');
                                            optTreeGrid.reload();
                                        } else {
                                            notify('删除失败.');
                                        }
                                    });
                                }
                            });
                        } else {
                 		   mini.alert("不能删除有子节点的父节点", "提示", function callback(action){return;});
                        }
                    });
                } else {
                    notify('操作失败.');
                }
            });
        	}
        } else {
        
  		   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
        }
    }
    function saveOpt(flag) {
        var url = "";
        var form;
        if ("f_insert" == flag) {
            form = new mini.Form("#addform");
            var node = optTreeGrid.getSelectedNode();
            if (node) {
                url = "${ctx}/rbac/menue/createByPare/" + node.menue_id;
            } else {
                url = "${ctx}/rbac/menue/create";
            }
        } else if ("f_edit" == flag) {
            form = new mini.Form("#editform");
            url = "${ctx}/rbac/menue/update";
        }
        form.validate();
        if (form.isValid() == true) {
            var o = form.getData();
            $.post(url, o, function (msg) {
                if (msg==true) {
                    if ("f_insert" == flag) {
                        notify("添加成功");
                    } else if ("f_edit" == flag) {
                    	 notify("编辑成功");
                    }
                    mini.confirm("操作成功，请重新登录。", "提示", function (action) {
                        if (action == "ok") {
                            var logoutUrl = '${ctx}/logout';
                            window.parent.location = logoutUrl;
                        } else {
                            optTreeGrid.reload();
                        }
                    });
                } else {
                    notify("服务器繁忙，请稍后重试");
                    optTreeGrid.reload();
                }
            });
            editWindow.hide();
            addWindow.hide();
        }
    }
    
    function move(flag){
 	   var row = optTreeGrid.getSelected();
 	   if(!row){
 			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
 	   }else{
 		   	if ("up" == flag) {
 	    	   $.post("${ctx}/rbac/menue/getMinIndex/" + row.par_menue_id, function (result) {
 		   		//判断第一位
 			   if(row.menue_order==result.minIndex){ 
 				   mini.alert("当前已经处于第一位！", "提示", function callback(action){return;});
 		        }else{  
 		        	//排序调整
 		        	 $.post("${ctx}/rbac/menue/up/" + row.menue_id+"/"+row.par_menue_id, function (result) {
 		        			optTreeGrid.reload();
 		            });
 		        }
           	   });
 	       } else if ("down" == flag) {
 	    	   $.post("${ctx}/rbac/menue/getMaxIndex/" + row.par_menue_id, function (result) {
 	    	   //判断最后一位
 	    	   if(row.menue_order==result.maxIndex){  
 				   mini.alert("当前已经处于最后一位！", "提示", function callback(action){return;});
 		        }else{  
 		        	//排序调整
 		        	 $.post("${ctx}/rbac/menue/down/" + row.menue_id+"/"+row.par_menue_id, function (result) {
 		        			optTreeGrid.reload();
 		        	 });
 		        }
            	   });
 	       }
 	   }
    }
    //////////// /////////////////////////////////////
    function onStatusRenderer(e) {
        <c:forEach var="status" items='${commTypeList}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }

    function onNmInValidation(e) {
        if (e.isValid) {
            var row = optTreeGrid.getSelected();
            var fun_opt_nm = row.fun_opt_nm;
            if (fun_opt_nm != e.value) {
                $.ajax({
                    url: '${ctx}/rbac/menue/check',
                    async: false,
                    data: {fun_opt_nm: e.value},
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
    }
    function onNmValidation(e) {
        if (e.isValid) {
            $.ajax({
                url: '${ctx}/rbac/menue/check',
                async: false,
                data: {fun_opt_nm: e.value},
                success: function (text) {
                    if (!text) {
                        e.errorText = "该名称已经存在.'/>!";
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
    

    //添加时的验证
    function onInsertBeforeNodeSelect(e) {
    	 var row = optTreeGrid.getSelected();
    	 //选中
         if (row) {
        	 //选中父节点添加菜单时禁止选中父节点
        	 if (row.par_menue_id == 1) {
            	 if (e.isLeaf == false) e.cancel = true;
        	 }
         } else {//未选中
            //未选中任何节点默认只能选择父节点
        	 if (e.isLeaf == true) e.cancel = true;
         }

    }
    
    //修改时的验证
     function onEditBeforeNodeSelect(e) {
    	 var row = optTreeGrid.getSelected();
    	 //选中
         if (row) {
        	 var tree = e.sender;
             var nowNode = e.node;
             var parent_fun_opt_id=nowNode.parent_fun_opt_id;
        	 //选中父节点修改菜单时禁止选中子节点
        	 if (row.par_menue_id == 1) {
            	 if (parent_fun_opt_id != undefined) e.cancel = true;
            	 //console.info(level);
            	 
        	 }else{
        		//选中子节点修改时禁止选择父节点
        		console.info(parent_fun_opt_id == undefied);
            	 if (parent_fun_opt_id == undefined) e.cancel = true;
            	 //console.info(level);
            	 alert('aaa');
        	 }
         } 

    }

</script>
</body>
</html>