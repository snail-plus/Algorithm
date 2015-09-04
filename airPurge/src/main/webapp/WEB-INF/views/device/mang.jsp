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
     <script src="http://api.map.baidu.com/api?v=2.0&ak=d1L9YY7ID9GCFLTXw8Di1Ieg" type="text/javascript"></script>
</head>
<body>
        
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a class="mini-button" iconCls="icon-add" onclick="edit('f_insert')">增加</a>
                <a class="mini-button" iconCls="icon-edit" onclick="edit('f_edit')">编辑</a>
                <a class="mini-button" iconCls="icon-remove" onclick="remove()">删除</a>
                <a class="mini-button" iconCls="icon-role" onclick="bandRoleInfo()">绑定用户</a>
                <a class="mini-button" iconCls="icon-role" onclick="previewQR()">查看二维码</a>
                <a class="mini-button" iconCls="icon-edit" onclick="editAddress">编辑地址</a>
                <a class="mini-button" iconCls="icon-edit" onclick="toPrintQR">打印二维码</a>
            </td>
        </tr>
    </table>
</div>

<div class="mini-fit">
    <div id="optTreeGrid" class="mini-treegrid" style="width:100%;height:100%;" url="${ctx}/device/mang/listAll"
         showTreeIcon="true" treeColumn="device_guid" idField="device_id" parentField="par_device_id" resultAsTree="false"
         allowResize="true" expandOnLoad="true">
        <div property="columns">
            <div type="indexcolumn"></div>
            <div name="device_guid" field="device_guid" width="260">设备编号</div>
            <div name="remarks" field="remarks" width="260">设备名称</div>
            <div field="record_time" width="100" dateFormat="yyyy-MM-dd HH:mm:ss" headerAlign="center" align="center" allowSort="true">录入时间</div>
            <div field="status" renderer="onStatusRenderer" width="240">状态</div>
        </div>
    </div>
</div>

<div id="editWindow" class="mini-window" title="编辑" style="width:380px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="editform" class="form">
        <input class="mini-hidden" name="device_id"/>
        <input class="mini-hidden" name="par_device_id"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">设备编号:</td>
                <td style="width:150px;">
                    <input name="device_guid" style="width: 150px;" class="mini-textbox" onvalidation="onNmInValidation"
                        readonly="true" required="true" vtype="maxLength:40"/>
                </td>
            </tr>
            
            <tr>
                <td style="width:80px;">设备名称:</td>
                <td style="width:150px;">
                    <input name="remarks" style="width: 150px;" class="mini-textbox" vtype="maxLength:64" required="true"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">设备状态:</td>
                <td style="width:150px;">
                    <input name="status"  textField="text" required="true" url="${ctx}/base/dict/get/COMM_TYPE"
                       valueField="id" allowInput="false" class="mini-combobox" /></td>
                </td>
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
        <input class="mini-hidden" id="a_par_device_id" name="par_device_id"/>
        <input class="mini-hidden" id="a_device_level" name="device_level"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">设备标示:</td>
                <td style="width:150px;">
                    <input id="device_guid" name="device_guid" class="mini-textbox" style="width: 150px;"
                           readOnly="true" required="true" vtype="maxLength:40" />
                </td>
            </tr>
             <tr>
                <td style="width:80px;">备注:</td>
                <td style="width:150px;">
                    <input name="remarks" style="width: 150px;" class="mini-textbox" vtype="maxLength:64" required="true"  />
                </td>
            </tr>
             <tr>
                <td style="width:80px;">设备状态:</td>
                <td style="width:150px;">
                    <input name="status"  textField="text" required="true" url="${ctx}/base/dict/get/COMM_TYPE"
                       valueField="id" allowInput="false" class="mini-combobox" /></td>
                </td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="saveOpt('f_insert')" style="width: 60px; margin-right: 20px;">确认</a>
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        </div>
    </div>
</div>

<!-- device bind start -->

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
<!-- device bind end -->

<!-- qr start -->
<div id="qrPreviewWindow" class="mini-window" title="二维码查看" style="width:215px;height:240px;" showModal="true" allowResize="true"
     allowDrag="true" >
    <div class="form" style="overflow:hidden">
         <img id="qrInfo" style="width:200px;height:200px;"/>
    </div>
</div>
<!-- qr end -->

<script type="text/javascript" src="${js}/sockjs-0.3.4.min.js"></script>
<script type="text/javascript">
    mini.parse();
  
    var optTreeGrid = mini.get("optTreeGrid");
   
    var addWindow = mini.get("addWindow");
    var editWindow = mini.get("editWindow");
    var roleBindWindow = mini.get("roleBindWindow");
    var optTreeSelect=mini.get("optTreeSelect");
    var listUser = mini.get("listUser");
    var listRole = mini.get("listRole");
    var qrPreviewWindow = mini.get("qrPreviewWindow");
    function edit(flag) {
        if ("f_insert" == flag) {
            var form = new mini.Form("#addform");
            form.reset();
            var row = optTreeGrid.getSelected();
            if (row) {
                if (row.device_level == 1 || row.device_level == 2) {
                	mini.get("a_par_device_id").setValue(row.device_id);
                    mini.get("a_device_level").setValue(row.device_level + 1);
                	$.post("${ctx}/device/mang/getDeviceGuid/", {par_device_id:row.device_id,device_level:row.device_level + 1}, function (result) {
                        if (result) {
                           mini.get("device_guid").setValue(result);
                           
                        } else {
                            mini.alert("获取设备标示失败");
                        }
                    });
                    addWindow.show();
                } else {
               	  mini.alert("不能在该节点下添加子节点", "提示", function callback(action){return;});
                }
            } else {
            	 $.post("${ctx}/device/mang/getDeviceGuid/", {par_device_id:0,device_level:1}, function (result) {
            		 mini.get("a_par_device_id").setValue(0);
            		 mini.get("a_device_level").setValue(1);
                     if (result) {
                    	 mini.get("device_guid").setValue(result);
                     } else {
                         mini.alert("获取设备标示失败");
                     }
                 });
                addWindow.show();
            }

        } else if ("f_edit" == flag) {
            var row = optTreeGrid.getSelected();
            console.info(row._level);
            if (row) {
                editWindow.show();
                var form = new mini.Form("#editform");
                form.clear();
                form.loading();
                $.ajax({
                    url: '${ctx}/device/mang/getDeviceById/' + row.device_id,
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
        editWindow.hide();
        addWindow.hide();
        roleBindWindow.hide();
    }
    function remove() {
        var row = optTreeGrid.getSelected();
        if (row) {
        	if(row.state=='1'){
      		   mini.alert("不能删除有效的菜单.", "提示", function callback(action){return;});
        	}else{
            //删除之前验证所选记录是否存在
            $.post("${ctx}/device/mang/fredelcheck/" + row.device_id, function (msg) {
                if (msg) {
                    $.post("${ctx}/device/mang/checkChild/" + row.device_id, function (checkMsg) {
                        if (checkMsg) {
                            mini.confirm("确定删除记录吗?", "提示", function (action) {
                                if (action == 'ok') {
                                    //删除选中记录
                                    $.post("${ctx}/device/mang/deleteMenue/" + row.device_id, function (result) {
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
                url = "${ctx}/device/mang/createByPare/" + node.device_id;
            } else {
                url = "${ctx}/device/mang/create";
            }
        } else if ("f_edit" == flag) {
            form = new mini.Form("#editform");
            url = "${ctx}/device/mang/update";
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
                    optTreeGrid.reload();
                } else {
                    notify("服务器繁忙，请稍后重试");
                }
            });
            editWindow.hide();
            addWindow.hide();
        }
    }
    
    //绑定角色
    function bandRoleInfo() {
        var row = optTreeGrid.getSelected();
        if (row) {
            $.ajax({
                url: "${ctx}/device/mang/getAllUser/" + row.device_id,
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
        } else {
       	  mini.alert("请选择一条记录", "提示", function callback(action){return;});
        }
    }
    
    //bind user
    function doBandRoleInfo() {
    	 var row = optTreeGrid.getSelected();
        var value = listRole.getValue();
        var url = "${ctx}/device/mang/insertDeviceUser/";
        $.post(url + row.device_id, {'boundInfos': value}, function (data) {
            if (data) {
                notify("绑定成功");
                roleBindWindow.hide();
            } else {
                roleBindWindow.hide();
                notify("绑定失败");
            }
        });
    }
    
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
    
    
  //编辑地址
    function editAddress(e){
    	 var row = optTreeGrid.getSelected();
    	   if (row) {
    		   console.info(row);
    		  if(row.device_level != 1){
    	    		 mini.alert("非顶级设备 不允许编辑地址信息","提示", function callback(action){return;});
    	    		 return;
    		  }
    		   mini.open({
                   url: "${ctx}/device/mang/toeditJsp/" ,
                   title: "编辑设备所在地址",
                   height: 450,
                   width: 500,
                   onload: function () {
                	   var iframe = this.getIFrameEl();
     	   	           var data = { action: "editAddress", id: row.device_id };
     	   	           iframe.contentWindow.SetData(data); 
                   },
                   ondestroy: function (action) {
                	   optTreeGrid.reload();
                   }
               });
    	   }else{
    		 mini.alert("请先选择一条记录", "提示", function callback(action){return;});
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
        	 }
         } 

    }

    function previewQR(){
    	 var row = optTreeGrid.getSelected();
    	 console.info(row);
    	 if(row.device_level != '3'){
    		 mini.alert("所选设备暂无二维码.", "提示", function callback(action){return;});
    	 }else{
             $("#qrInfo").attr("src", row.qrcode_url);
    		 qrPreviewWindow.show();
    	 }
    }
    
    function toPrintQR(){
	   	 var row = optTreeGrid.getSelected();
	   	 console.info(row);
	   	 if(row.device_level != '3'){
	   		 mini.alert("所选设备暂无二维码.", "提示", function callback(action){return;});
	   	 }else{
	   		 window.open("${ctx}/device/mang/printQR?device_guid=" + row.device_guid);
	   	 }
   }
</script>


</body>
</html>