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
<div style="padding:5px 0 5px 0">
    <span>字典编码:</span><input type="text" id="search_eq_dict_id" /> 
    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <span>字典名称:</span><input type="text" id="search_like_dict_name" /> 
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
		<table style="width: 100%;min-width: 900px">
			<tr>
				<td>
                       <a class="mini-button" iconCls="icon-add" onclick="edit('f_insert')">新增</a>
                       <a class="mini-button" iconCls="icon-edit" onclick="edit('f_edit')">编辑</a>
                       <a class="mini-button" iconCls="icon-remove" onclick="remove()">删除</a>
                       <a class="mini-button" iconCls="icon-search" onclick="getDetail()">明细</a>
				</td>
			</tr>
		</table>
	</div>    
        
    <div class="mini-fit" >
    <div id="dictDataGrid" class="mini-datagrid" style="width:100%;height:100%;"  allowResize="true"
        url="${ctx}/base/dict/list"  idField="dict_id" multiSelect="true" >
        <div property="columns">
        	<div type="indexcolumn"></div>
            <!-- <div type="checkcolumn"></div> -->
            <div field="dict_id"  headerAlign="center" allowSort="true">字典编码</div>    
            <div field="dict_name"  headerAlign="center" allowSort="true">字典名称</div>    
            <div field="dict_desc" headerAlign="center" allowSort="true" >字典描述</div>    
            <div field="dict_status" headerAlign="center" allowSort="true" renderer="onStatusRenderer" >是否有效</div>    
        </div>
    </div>
    </div>
    <div id="addWindow" class="mini-window" title="新增" style="width:320px;"  showModal="true" allowResize="true" allowDrag="true" >
	    <div id="addform" class="form" >
	        <table style="width:100%;">
	            <tr>
	                <td style="width:80px;">字典编码：</td>
	                <td style="width:200px;">
	                	<input name="dict_id" class="mini-textbox" onvalidation="onNmValidation" required="true" vtype="upperEnglish" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/>
	                </td>
	            </tr>
	            <tr>
	                <td style="width:80px;">字典名称：</td>
	                <td style="width:200px;"><input name="dict_name" class="mini-textbox" required="true" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/></td>
	            </tr>
	            <tr>
	                <td style="width:80px;">字典描述：</td>
	                <td style="width:200px;"><input name="dict_desc"   class="mini-textarea" required="true" vtype="maxLength:128" minLengthErrorText="不能多余于128个字符"/></td>
	            </tr>
	            <tr>
	                <td style="width:80px;">是否有效：</td>
	                <td style="width:200px;"><input name="dict_status"   url="${ctx}/base/dict/get/COMM_TYPE"  class="mini-combobox" required="true"/></td>
	            </tr>
	         
	        </table>
	          <div style="text-align: center; padding: 10px;">
	              <a class="mini-button" href="javascript:save('f_insert')" style="width: 60px; margin-right: 20px;">确定</a> 
	              <a class="mini-button" href="javascript:cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        	</div>
	    </div>
	</div>
	<div id="editWindow" class="mini-window" title="编辑" style="width:350px;"  showModal="true" allowResize="true" allowDrag="true" >
	    <div id="editform" class="form" >
	        <input class="mini-hidden" name="dict_id"/>
	        <table style="width:100%;">
	            <tr>
	                <td style="width:80px;">字典名称：</td>
	                <td style="width:200px;"><input name="dict_name"  class="mini-textbox" required="true" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/></td>
	            </tr>
	            <tr>
	                <td style="width:80px;">字典描述：</td>
	                <td style="width:200px;"><input name="dict_desc"   class="mini-textarea" required="true" vtype="maxLength:64" minLengthErrorText="不能多余于64个字符"/></td>
	            </tr>
	            <tr>
	                <td style="width:80px;">是否有效：</td>
	                <td style="width:200px;"><input name="dict_status" url="${ctx}/base/dict/get/COMM_TYPE" class="mini-combobox" required="true" vtype="maxLength:128" minLengthErrorText="不能多余于128个字符"/></td>
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
        
        //字典新增  编辑
        function edit(flag) {
        	if("f_insert" == flag ){
        		var form = new mini.Form("#addform");
            	form.reset();
            	addWindow.show();
        	}else if("f_edit" == flag ){
        		var row = grid.getSelected();
                if (row) {
                    editWindow.show();
                    var form = new mini.Form("#editform");
                    form.clear();
                    form.loading();
                    $.ajax({
                        url: '${ctx}/base/dict/' + row.dict_id,
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
                }else{
     			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
                }
        	}
        }
        //查询
        function search() {
        	var search_eq_dict_id = $("#search_eq_dict_id").val();
        	var search_like_dict_name = $("#search_like_dict_name").val();
        	/* var search_eq_dict_type = $("#search_eq_dict_type").val();
        	var search_eq_dict_status = $("#search_eq_dict_status").val(); */
        	grid.load({
        		dict_id: search_eq_dict_id, 
        		dict_name: search_like_dict_name
        		/* search_eq_dict_type: search_eq_dict_type, 
        		search_eq_dict_status: search_eq_dict_status */
        	});
        }
        
        function reset() {
        	$("#search_eq_dict_id").val("");
        	$("#search_like_dict_name").val("");
        	/* $("#search_eq_dict_type").val("");
        	$("#search_eq_dict_status").val(""); */
        }
        function cancel() {
            addWindow.hide();
            editWindow.hide();
        }
        //字典删除
        function remove() {   
	       	 var row = grid.getSelected();
	       	 if(row){//判断菜单是否有效
	         	if(row.dict_status=='1'){
	       		   mini.alert("不能删除有效的字典编码.", "提示", function callback(action){return;});
	         	}else{	
	       	  mini.confirm("您确定要删除此记录吗?", "提示", function (msg) {
                  if (msg == 'ok') {
	       			
	       		//删除选中记录
					$.post("${ctx}/base/dict/delete/"+ row.dict_id,function(result) {
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
	       	 }else {
   			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
	            }
        }
        //字典编辑  编辑
        function save(flag) {   
       	 var url = "";
       	 var form;
            if("f_insert" == flag ){
            	form = new mini.Form("#addform");
               	url = "${ctx}/base/dict/create";
            }else if("f_edit" == flag ){
            	 form = new mini.Form("#editform");
            	 url = "${ctx}/base/dict/update";
            }
           form.validate();
           if (form.isValid() == true) {
           	var o = form.getData();
           	//optTreeGrid.loading("保存中，请稍后......");
               var json = mini.encode(o);
               $.ajax({
                   url: url,
                   type:"post", 
                   dataType:"json",      
                   contentType:'application/json;charset=UTF-8',  
                   data:json, 
                   success: function (text) {
                   	if(text){
                   		 if("f_insert" == flag ){
                   			 notify("添加成功");
                   		 }else if("f_edit" == flag ){
                   			 notify("编辑成功");
                   		 }
                   		grid.reload();
                   	}else{
                   		notify("服务器繁忙，请稍后重试");
                   		grid.reload();
                   	}
                   	
                   },
                   error: function (jqXHR, textStatus, errorThrown) {
                   	notify("服务器繁忙，请稍后重试");
                   	grid.reload();
                   }
               });  
               editWindow.hide(); 
               addWindow.hide();
           }
       }
        
        //字典详情
        function getDetail(){
        	var row = grid.getSelected();
	       	 if(row){
				window.location.href = "${ctx}/base/dictDetail?dictId="+row.dict_id;	       				
	       	 }else {
	   			   mini.alert("请先选择一条记录", "提示", function callback(action){return;});
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
        
        
        function onNmValidation(e) {
            if (e.isValid) {
            		$.ajax({
            			url: '${ctx}/base/dict/check',
                        async : false, 
                        data:{dictId:e.value},
                        success: function (text) {
                       	 if(!text){
                       		e.errorText = "该字典名称已经存在!";
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
        
        /*自定义vtype*/
        mini.VTypes["upperEnglishErrorText"] = "请输入大写英文字母";
        mini.VTypes["upperEnglish"] = function (v) {
            var re = new RegExp("^[A-Z\_]+$");
            if (re.test(v)) return true;
            return false;
        }


    </script>
</body>
</html>