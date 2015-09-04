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
    <span>反馈时间:</span><input type="text" id="start_time" class="mini-datepicker" allowInput="false"/> 
    <span>&nbsp;&nbsp;-&nbsp;&nbsp;</span>
    <input type="text" id="end_time" class="mini-datepicker" allowInput="false"/> 
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>
<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
		<table style="width: 100%;min-width: 900px">
			<tr>
				<td>
                       <a class="mini-button" iconCls="icon-edit" onclick="edit()">备注</a>
				</td>
			</tr>
		</table>
	</div>    
        
    <div class="mini-fit" >
    <div id="feedbackDataGrid" class="mini-datagrid" style="width:100%;height:100%;"  allowResize="true"
        url="${ctx}/base/feedback/list"  idField="feedback_id" multiSelect="true" >
        <div property="columns">
        	<div type="indexcolumn"></div>
            <!-- <div type="checkcolumn"></div> -->
            <div field="content"  headerAlign="center" allowSort="true" width="200">反馈内容</div>    
            <div field="user_name" headerAlign="center" allowSort="true"  width="50">反馈用户</div>   
            <div field="feedback_time"  headerAlign="center" allowSort="true" width="50" dateFormat="yyyy-MM-dd HH:mm:ss">反馈时间</div> 
            <div field="remark" headerAlign="center" allowSort="true" width="200">备注</div>    
            <div field="version" headerAlign="center" allowSort="true" width="50" >版本号</div>    
        </div>
    </div>
    </div>
   
	<div id="editWindow" class="mini-window" title="编辑" style="width:50%;height:auto;"  showModal="true" allowResize="true" allowDrag="true" >
	    <div id="editform" class="form" >
	        <input class="mini-hidden" name="feedback_id"/>
	        <table style="width:100%;">
	            <tr>
	                <td style="width:30px;">反馈内容：</td>
	                <td style="width:200px;"><input name="content" id="content"  class="mini-textarea" allowInput="false" style="width: 100%;height: 80px;"  /></td>
	            </tr>
	            <tr>
	                <td style="width:30px;">反馈用户</td>
	                <td style="width:200px;"><input style="width: 200px;" name="user_name" id="human_name"  class="mini-textbox" allowInput="false" /></td>
	            </tr>
	            <tr>
	                <td style="width:30px;">反馈时间：</td>
	                <td style="width:200px;"><input style="width: 200px;" name="feedback_time" id="feedback_time" class="mini-datepicker" allowInput="false"   format="yyyy-MM-dd HH:mm:ss" /></td>
	            </tr>
	             <tr>
	                <td style="width:30px;">备注：</td>
	                <td style="width:200px;"><input name="remark"  class="mini-textarea" style="width: 100%;height: 80px;"  maxlength="255"/>
	                <br>(最多输入255个字符)</br>
	                </td>
	                
	            </tr>
	              <tr>
	                <td style="width:30px;">版本号</td>
	                <td style="width:200px;"><input style="width: 200px;" name="version" id="version"  class="mini-textbox" allowInput="false" /></td>
	            </tr>
	        </table>
	         <div style="text-align: center; padding: 10px;">
	             <a class="mini-button" href="javascript:save()" style="width: 60px; margin-right: 20px;">确定</a> 
	             <a class="mini-button" href="javascript:cancel()" style="width: 60px; margin-right: 20px;">取消</a>
        	</div>
	    </div>
	</div>
    
    <script type="text/javascript">
        mini.parse();

        var feedbackDataGrid = mini.get("feedbackDataGrid");
        feedbackDataGrid.load();
        feedbackDataGrid.sortBy("feedback_id", "desc");
        var editWindow = mini.get("editWindow");
        
        var feedback_time = mini.get("feedback_time"); 
        feedback_time.disable();
        var content = mini.get("content"); 
        content.disable();
        var human_name = mini.get("human_name"); 
        human_name.disable();
        var version = mini.get("version"); 
        version.disable();
        // 编辑
        function edit() {
        		var row = feedbackDataGrid.getSelected();
                if (row) {
                    editWindow.show();
                    var form = new mini.Form("#editform");
                    form.clear();
                    form.loading();
                    $.ajax({
                        url: '${ctx}/base/feedback/edit/' + row.feedback_id,
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
        //查询
        function search() {
        	var start_time=mini.get("start_time").getFormValue();
        	var end_time=mini.get("end_time").getFormValue();
        	if(start_time.length==0 && end_time.length!=0){
  			   mini.alert("请选择起始日期!", "提示", function callback(action){return;});
        	}else if(start_time.length!=0&&end_time.length==0){
   			   mini.alert("请选择截止日期!", "提示", function callback(action){return;});
        	}else if(start_time.length!=0&&end_time.length!=0){  
        		if(!checkEndTime()){  
        			mini.alert("截止日期必须大于起始日期！", "提示", function callback(action){return;});
        		    return;  
        		} else{
		        	feedbackDataGrid.load({
		        		"start_time": mini.get("start_time").getFormValue()+" 00:00:00", 
		        		"end_time": mini.get("end_time").getFormValue()+" 23:59:59"
		        	});
        		}
        	}else {
        		feedbackDataGrid.load({
	        		"start_time": mini.get("start_time").getFormValue(), 
	        		"end_time": mini.get("end_time").getFormValue()
	        	});
        	}
        }
        
        //判断日期大小
        function checkEndTime(){  
            var startTime=mini.get("start_time").getFormValue();  
            var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
            var endTime=mini.get("end_time").getFormValue();
            var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
            if(end<start){  
                return false;  
            }  
            return true;  
        }  
        
        function reset() {
        	 mini.get("start_time").setValue("");     
     		 mini.get("end_time").setValue("");
        }
        function cancel() {
            editWindow.hide();
        }
      
        function save() {   
       	 var form =new mini.Form("#editform");
           form.validate();
           if (form.isValid() == true) {
        	  	var o = form.getData();
        	  	   o.feedback_time=mini.get("feedback_time").getFormValue();
                   var json = mini.encode(o);
                   $.ajax({
                       url: "${ctx}/base/feedback/update",
                       type:"post", 
                       dataType:"json",      
                       contentType:'application/json;charset=UTF-8',  
                       data:json, 
                       success: function (text) {
                       	if(text){
                       		notify("编辑成功");
                       		feedbackDataGrid.reload();
                       	}else{
                       		notify("服务器繁忙，请稍后重试");
                       		feedbackDataGrid.reload();
                       	}
                       	
                       },
                       error: function (jqXHR, textStatus, errorThrown) {
                       	notify("服务器繁忙，请稍后重试");
                       	feedbackDataGrid.reload();
                       }
                   });  
                   editWindow.hide(); 
           }
       }
        

    </script>
</body>
</html>