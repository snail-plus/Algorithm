<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
<jsp:include page="/meta.jsp" />
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
	<div class="mini-toolbar" style="border-bottom: 0; padding: 0px;">
		<table style="width: 100%;">
			<tr>
				<td style="width: 100%;">
				<a class="mini-button" iconCls="icon-add" onclick="edit('f_insert')">新增</a> 
				<a class="mini-button" iconCls="icon-edit" onclick="edit('f_edit')">编辑</a>
				<a class="mini-button" iconCls="icon-remove" onclick="remove()">删除</a>
				<a class="mini-button" iconCls="icon-upgrade" onclick="reback()">返回</a>
				</td>
			</tr>
		</table>
	</div>
	<div class="mini-fit">
		<div id="dictDataGrid" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="true"
			url="${ctx}/base/dictDetail/list?dictId=${dictId}" idField="dict_id" multiSelect="true">
			<div property="columns">
				<div type="indexcolumn"></div>
				<div field="dict_id"  headerAlign="center" allowSort="true" >字典编码</div>
				<div field="dict_param_name"  headerAlign="center" allowSort="true" >字典参数名称</div>
				<div field="dict_param_value"   headerAlign="center" allowSort="true">字典参数值</div>
				<div field="dict_param_name_en"  headerAlign="center" allowSort="true" >字典参数名称英文</div>
				<div field="dict_param_status"   headerAlign="center" allowSort="true" renderer="onStatusRenderer" >是否有效</div>
			</div>
		</div>
	</div>
	<div id="addWindow" class="mini-window" title="新增" style="width: 320px;" showModal="true" allowResize="true" allowDrag="true">
		<div id="addform" class="form">
				<input class="mini-hidden" name="dict_id" value="${dictId}"/>
			<table style="width: 100%;">
				<tr>
					<td style="width: 80px;">字典参数名称：</td>
					<td style="width: 200px;">
						<input name="dict_param_name" class="mini-textbox" required="true"  vtype="maxLength:64" minLengthErrorText="字典参数名称不能多余于64个字符"/>
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">字典参数名称英文：</td>
					<td style="width: 200px;">
						<input name="dict_param_name_en" class="mini-textbox" required="true"   onvalidation="onEnglishValidation" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/>
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">字典参数值：</td>
					<td style="width: 200px;">
						<input name="dict_param_value" class="mini-textbox" required="true" onvalidation="onNmValidation"/>
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">是否有效：</td>
					<td style="width: 200px;">
						<input name="dict_param_status" url="${ctx}/base/dict/get/COMM_TYPE"  class="mini-combobox" required="true" />
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 10px;">
	             <a class="mini-button" href="javascript:save('f_insert')" style="width: 60px; margin-right: 20px;">确定</a> 
				 <a class="mini-button" href="javascript:cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        	</div>
		</div>
	</div>
	<div id="editWindow" class="mini-window" title="编辑" style="width: 350px;" showModal="true" allowResize="true" allowDrag="true">
		<div id="editform" class="form">
			<input class="mini-hidden" name="dict_id" value="${dictId}"/>
			<table style="width: 100%;">
				<tr>
					<td style="width: 80px;">字典参数名称：</td>
					<td style="width: 200px;"><input name="dict_param_name" vtype="maxLength:64" minLengthErrorText="字典参数名称不能多余于64个字符"
						class="mini-textbox" required="true" /></td>
				</tr>
				<tr>
					<td style="width: 80px;">字典参数值：</td>
					<td style="width: 200px;">
						<input name="dict_param_value" class="mini-textbox" required="true"  id="dict_param_value" onvalidation="onNmValidation"/>
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">字典参数名称英文：</td>
					<td style="width: 200px;">
						<input name="dict_param_name_en" class="mini-textbox" required="true" onvalidation="onEnglishValidation" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/>
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">是否有效：</td>
					<td style="width: 200px;"><input name="dict_param_status"
						url="${ctx}/base/dict/get/COMM_TYPE" class="mini-combobox"
						required="true" /></td>
				</tr>
			</table>
			<div style="text-align: center; padding: 10px;">
	             <a class="mini-button" href="javascript:save('f_edit')" style="width: 60px; margin-right: 20px;">确定</a> 
				 <a class="mini-button" href="javascript:cancel()" style="width: 60px; margin-right: 20px;">取消</a>
				 
        	</div>
		</div>
	</div>


	<script type="text/javascript">
		mini.parse();

		var grid = mini.get("dictDataGrid");
		grid.load();
		grid.sortBy("dict_id", "asc");
		var editWindow = mini.get("editWindow");
		var addWindow = mini.get("addWindow");

		
		 var dict_param_value = mini.get("dict_param_value"); 
		 dict_param_value.disable();
	        
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
						url : '${ctx}/base/dictDetail/' + row.dict_id + "/" + row.dict_param_value + "/" + row.dict_param_name,
						success : function(text) {
							form.unmask();
							var o = mini.decode(text);
							form.setData(o);
						},
						error : function() {
			    	   mini.alert("表单加载错误", "提示", function callback(action){return;});
							form.unmask();
						}
					});
				} else {
		   			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
				}
			}

		}

		function search() {
			var search_eq_dictId = $("#search_eq_dictId").val();
			var search_like_dictName = $("#search_like_dictName").val();
			var search_eq_dictType = $("#search_eq_dictType").val();
			var search_eq_dictStatus = $("#search_eq_dictStatus").val();
			grid.load({
				dict_id : search_eq_dictId,
				dict_name : search_like_dictName,
				dict_type : search_eq_dictType,
				dict_status : search_eq_dictStatus
			});
		}
		function reset() {
			$("#search_eq_dictId").val("");
			$("#search_like_dictName").val("");
			$("#search_eq_dictType").val("");
			$("#search_eq_dictStatus").val("");
		}
		function cancel() {
			addWindow.hide();
			editWindow.hide();
		}
		function remove() {
			var row = grid.getSelected();
			if (row) {	 
	         	if(row.dict_param_status=='1'){
		       		   mini.alert("不能删除有效的字典明细", "提示", function callback(action){return;});
		         	}else{	
				  mini.confirm("您确定要删除此记录吗?", "提示", function (msg) {
                      if (msg == 'ok') {
					//删除选中记录
					$.post("${ctx}/base/dictDetail/delete/" + row.dict_id + "/"
							+ row.dict_param_value, function(result) {
						if (result) {
							notify('删除成功');
							grid.reload();
						} else {
							notify('删除失败');
						}
					});
                      }
                  });
		         	}
			} else {
	   			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
			}
		}
		function save(flag) {
			var url = "";
			var form;
			if ("f_insert" == flag) {
				form = new mini.Form("#addform");
				url = "${ctx}/base/dictDetail/create";
			} else if ("f_edit" == flag) {
				form = new mini.Form("#editform");
				url = "${ctx}/base/dictDetail/update";
			}
			form.validate();
			if (form.isValid() == true) {
				var o = form.getData();
				//optTreeGrid.loading("保存中，请稍后......");
				var json = mini.encode(o);
				$.ajax({
					url : url,
					type : "POST",
					dataType : "json",
					contentType : 'application/json;charset=UTF-8',
					data : json,
					success : function(text) {
						if (text) {
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
					},
					error : function(jqXHR, textStatus, errorThrown) {
						notify("服务器繁忙，请稍后重试");
						grid.reload();
					}
				});
				editWindow.hide();
				addWindow.hide();
			}
		}

		function reback() {
			location.href = "${ctx}/base/dict/";
		}
		/////////////////////////////////////////////////

		  function onStatusRenderer(e) {
         	<c:forEach var="status" items='${commTypeList}'>
        	 if('${status.id}'==e.value){
        		 return '${status.text}';
        	   }
 			</c:forEach>
    	 }

		
		  function onEnglishValidation(e) {
	            if (e.isValid) {
	                if (isEnglish(e.value) == false) {
	                    e.errorText = "必须输入英文、数字";
	                    e.isValid = false;
	                }
	            }
	        }
		  /* 是否英文 */
         function isEnglish(v) {
            var re = new RegExp("^[a-zA-Z0-9\_]+$");
            if (re.test(v)) return true;
            return false;
         }
		  
		function onNmValidation(e) {
			if (e.isValid) {
				 var upValue = e.value.toUpperCase();
				 this.setValue(upValue);
				 var regExp = /^[A-Z0-9]+$/
				if(regExp.test(upValue)){
					$.ajax({
						url : '${ctx}/base/dictDetail/check',
						async : false,
						data : {
							dictId : '${dictId}',
							dictParamValue:upValue
						},
						success : function(text) {
							if (!text) {
								e.errorText = "该字典参数值已经存在!";
								e.isValid = false;
							}
						},
						error : function() {
							notify("表单加载错误");
							e.isValid = false;
						}
					}); 
				}else{
					e.errorText = "字典值由大写英文字母、数字组成.";
					e.isValid = false;
				}			
				
			}
		}
		
	</script>
</body>
</html>