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
	        <span>设备编号:</span><input class="mini-textbox" id="search_device_guid"/>
    <input type="button" value="搜索" onclick="search()"/>
    <span>&nbsp;</span>
    <input type="button" value="重置" onclick="reset()"/>
</div>

<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
              <span>&nbsp;&nbsp;</span>
                 <a class="mini-button" iconCls="icon-node" onclick="dayComp()">室内外数据对比</a>
                 <a class="mini-button" iconCls="icon-node" onclick="timeComp">设备数据对比</a>
            </td>
	    </tr>
    </table>
</div>

<div class="mini-fit">
    <div id="logDategrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
         url="${ctx}/device/history/list" idField="user_id" multiSelect="true">
        <div property="columns">
            <div field="device_guid" width="180" headerAlign="center" allowSort="true">设备号</div>
            <div field="pm25" width="100" headerAlign="center" allowSort="true">pm2.5</div>
            <div field="pm10" width="100" headerAlign="center" allowSort="true">pm10</div>
            <div field="temp" width="100" headerAlign="center" allowSort="true">温度</div>
            <div field="hum" width="120" headerAlign="center" allowSort="true">湿度</div>
            <div field="remarks" width="120" headerAlign="center" allowSort="true">风机备注</div>
            <div field="record_time" width="120" headerAlign="center" allowSort="true" dateFormat="yyyy-MM-dd HH:mm:ss" >时间</div>
            </div>
        </div>
    </div>
   
   <div id="resultdWindow" class="mini-window" title="室内外数据对比" style="width: 850px; height: 400px;" showModal="false" allowResize="false" allowDrag="false">
	 <div style="padding-bottom:5px;" class="form" id="recordForm">
			   <span>开始时间:</span><input style="width: 120px;" class="mini-datepicker" nullValue="null" showOkButton="true" showClearButton="false"
                      format="yyyy-MM-dd" showTime=""false"" id="start_time2" required="true" ondrawdate="onDrawDate"/> 
               <span>&nbsp;&nbsp;</span>
			   <span>结束时间:</span>
			   <input style="width: 120px;" class="mini-datepicker" nullValue="null" showOkButton="true" showClearButton="false"
                      format="yyyy-MM-dd" showTime="false" id="end_time2" required="true" ondrawdate="onDrawDate"/> 
               <span>&nbsp;&nbsp;</span>
               <span>设备:</span>
			   <input class="mini-combobox" style="width:100px;" id="serach_device" textField="text" valueField="id" url="${ctx}/device/history/getDevice" showNullItem="false" allowInput="false"/> <span>&nbsp;&nbsp;</span>
               <span>数据类型:</span>
			   <input class="mini-combobox" style="width:100px;" id="search_dataType2" textField="text" valueField="id" url="${ctx}/device/history/dataType" showNullItem="false" allowInput="false"/> <span>&nbsp;&nbsp;</span>
			   <input type="button" value="搜索" onclick="searchRecord()" />
			    <span>&nbsp;&nbsp;</span>
			   <input type="button" value="重置" onclick="resetRecord()" />
     </div>
     <div id="wrapper1">
	       <div id="chartDiv" style="width: 100%;height: 320px;"></div>
	 </div>
	</div>
	
   <div id="compWindow" class="mini-window" title="设备相关数据对比" style="width: 850px; height: 400px;" showModal="false" allowResize="false" allowDrag="false">
	   <div style="padding-bottom:5px;" class="form" id="searchForm">
			   <span>开始时间:</span><input style="width: 120px;" class="mini-datepicker" nullValue="null" showOkButton="true" showClearButton="false"
                      format="yyyy-MM-dd" showTime=""false"" id="startc" required="true" ondrawdate="onDrawDate" /> 
               <span>&nbsp;&nbsp;</span>
			   <span>结束时间:</span>
			   <input style="width: 120px;" class="mini-datepicker" nullValue="null" showOkButton="true" showClearButton="false"
                      format="yyyy-MM-dd" showTime="false" id="endc" required="true" ondrawdate="onDrawDate" /> 
               <span>&nbsp;&nbsp;</span>
               <span>设备：</span>
                <input id="select1" class="mini-treeselect" url="${ctx}/device/history/listTree" multiSelect="true" 
			        textField="text" valueField="id" parentField="pid" checkRecursive="true" 
			        showFolderCheckBox="false"  expandOnLoad="true" showClose="true" oncloseclick="onCloseClick"
			        popupWidth="200" 
			    />
			    <span>数据类型:</span>
			    <input class="mini-combobox" style="width:100px;" id="search_dataType" textField="text" valueField="id" url="${ctx}/device/history/dataType" showNullItem="false" allowInput="false"/> <span>&nbsp;&nbsp;</span>
			    
			   <input type="button" value="搜索" onclick="searchComp()" />
			   <input type="button" value="重置" onclick="resetComp()" />
        </div>
        <div id="wrapper2">
	        <div id="compDiv" style="width: 100%;height: 320px;"></div>
	    </div>
	</div>
	
<script type="text/javascript" src="${js}/highcharts.js"></script>
<script type="text/javascript">
    mini.parse();

    var grid = mini.get("logDategrid");
    grid.load();

    //搜索
    function search() {
        var startDate = mini.get("startDate").getFormValue();
        var endDate = mini.get("endDate").getFormValue();
        var device_guid = mini.get("search_device_guid").getValue();
        grid.load({
        	startDate: startDate,
        	endDate: endDate,
        	device_guid : device_guid
        });
    }

    //清空
    function reset() {
    	mini.get("startDate").setValue("");
    	mini.get("endDate").setValue("");
    	mini.get("search_device_guid").setValue("");
        grid.reload();
    }

    //////////// /////////////////////////////////////
    function onStatusRenderer(e) {
        <c:forEach var="status" items='${commTypeList}'>
       	 if('${status.id}'==e.value){
       		 return '${status.text}';
       	   }
		</c:forEach>
    }

    var resultdWindow = mini.get("resultdWindow");
	var recordForm = new mini.Form("recordForm");
	
	var compWindow = mini.get("compWindow");
	var searchForm = new mini.Form("searchForm");
	
	function timeComp(e){
	    $("#wrapper2").css("display","none"); 
		searchForm.clear();
		compWindow.show();
	}
	
	function dayComp(e){
		recordForm.clear();
	    $("#wrapper1").css("display","none"); 
		resultdWindow.show();
	}
	
	function onDrawDate(e) {
        var date = e.date;
        var d = new Date();
        if (date.getTime() > d.getTime()) {
            e.allowSelect = false;
        }
    }
	
	function showChart(data){
	    $("#wrapper1").css("display","block"); 
		$("#chartDiv").highcharts({
	        title: {
	            text: '室内外数据对比',
	            x: -20 
	        },
	        subtitle: {
	            text: '<span style=\"font-size: 11px\">小提示:点击最下面的设备名称可以隐藏/显示对应折线</span>'//,
	            //x: -20
	        },
	        xAxis: {
	            categories: data.xAxis,
	            labels: {
	                rotation: -90,
	                align: 'right',
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif'
	                }
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'pm2.5'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        credits : {
	    		enabled : false
	    	},
	        tooltip: {
	           // valueSuffix: '小时'
	        },
	        legend: {
	            layout: 'horizontal',
	            align: 'center',
	            verticalAlign: 'bottom',
	            borderWidth: 0
	        },
	        series: data.seriesData
	    });
      }
	
	 function beforenodeselect(e) {
         //禁止选中父节点
         if (e.isLeaf == false) e.cancel = true;
     }

     function onCloseClick(e) {
         var obj = e.sender;
         obj.setText("");
         obj.setValue("");
     }
     
	
	function showChart2(data){
	    $("#wrapper2").css("display","block"); 
		$("#compDiv").highcharts({
	        title: {
	            text: '设备数据对比',
	            x: -20 
	        },
	        subtitle: {
	            text: '<span style=\"font-size: 11px\">小提示:点击最下面的设备名称可以隐藏/显示对应折线</span>'//,
	            //x: -20
	        },
	        xAxis: {
	            categories: data.xAxis,
	            labels: {
	                rotation: -90,
	                align: 'right',
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif'
	                }
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'pm2.5'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        credits : {
	    		enabled : false
	    	},
	        tooltip: {
	           // valueSuffix: '小时'
	        },
	        legend: {
	            layout: 'horizontal',
	            align: 'center',
	            verticalAlign: 'bottom',
	            borderWidth: 0
	        },
	        series: data.seriesData
	    });
      }
	
	function searchRecord(){
		recordForm.validate();
		if(recordForm.isValid() == true){
		var start_time=mini.get("start_time2").getFormValue();
		var end_time=mini.get("end_time2").getFormValue();
		var dataType = mini.get("search_dataType2").getFormValue();
		var device_id = mini.get("serach_device").getFormValue();
			$.post("${ctx}/device/history/consmeRes/",{device_id:device_id,type:dataType},function(data){
    			if(data.seriesData.length > 0){
    				showChart(data);
    				resultdWindow.show();
    			}else{
                     notify("暂无记录");
    			}
    		});	   
		}
	}
	
	function searchComp(){
		var obj = mini.get("select1");
		if(recordForm.isValid() == true){
		//var start_time=mini.get("start_time2").getFormValue();
		//var end_time=mini.get("end_time2").getFormValue();
		var dataType = mini.get("search_dataType").getFormValue();
			$.post("${ctx}/device/history/compareDeviceData/",{device_guids:obj.getValue(),type:dataType},function(data){
    			if(data.seriesData.length > 0){
    				showChart2(data);
    				compWindow.show();
    			}else{
                     notify("暂无记录");
    			}
    		});	   
		}
	}
	
	

</script>
</body>
</html>