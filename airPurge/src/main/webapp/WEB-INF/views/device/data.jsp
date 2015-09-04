<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!doctype html>
<html>
<head>
 <meta http-equiv="X-UA-Compatible" content="IE=8"> 
 <title>空气净化</title>
<jsp:include page="/meta.jsp" />
<style type="text/css">
html,body {
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
	<div class="mini-layout" style="width: 100%; height: 100%;">
		 <div region="west" title="所有机构" showHeader="true" bodyStyle="padding-left:1px;" showSplitIcon="false" width="230"
          minWidth="100" maxWidth="350">
	         <ul id="menuTree" class="mini-tree" textField="text" idField="id"
	            showTreeIcon="true" style="width: 100%; height: 100%;"
	            enableHotTrack="true" onbeforeexpand="onBeforeExpand"
	            onnodeclick="onNodeSelect">
	         </ul>
         </div>
		 <div title="center" region="center" style="border: 0;">
			<div style="padding:5px 0 5px 0">
			    <span>设备名称:</span><input type="text" id="remarks"/>
			    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			    <span>设备编号:</span><input type="text" id="device_guid"/>
			    <input type="button" value="搜索" onclick="search()" />
			    <span>&nbsp;</span>	
                <input type="button" value="重置"  onclick="reset()" />
            </div>
            <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
			    <table style="width:100%;">
			        <tr>
			            <td style="width:100%;">
			                 <a class="mini-button" iconCls="icon-search" onclick="showDeviceData()">设备控制</a>
			            </td>
			        </tr>
			    </table>
            </div>
            
          <div class="mini-fit">
            <div id="communityDategrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
	         url="${ctx}/device/data/list" idField="community_id" multiSelect="true">
		         <div property="columns">
		            <div field="remarks" width="50" headerAlign="center" allowSort="true">设备名称</div>
		            <div field="device_guid" width="150" headerAlign="center" allowSort="true">设备编号</div>
		            <div field="pm25" width="60" headerAlign="center" allowSort="true">pm2.5</div>
		            <div field="pm10" width="50" headerAlign="center" allowSort="true">pm10</div>
		            <div field="temp" width="60" headerAlign="center" allowSort="true">温度</div>
		            <div field="hum" width="60" headerAlign="center" allowSort="true">湿度</div>
		            <div field="run_state" width="50" headerAlign="center" allowSort="true">设备运行状态</div>
		         </div>
            </div>
          </div>
		</div>
	</div>
	
          
<script type="text/javascript" src="${js}/angular.min.js"></script>
<script type="text/javascript" src="${js}/sockjs-0.3.4.min.js"></script>
<script type="text/javascript">
	mini.parse();
	var data =${orgData};
	var tree3 = mini.get("menuTree");
	tree3.loadData(data);
	
	var grid = mini.get("communityDategrid");
	grid.load();
	setInterval("grid.reload()",5000);
	var showDeviceWindow = mini.get("showDeviceWindow");
	
	
	function showDeviceData(e){
   	 var row = grid.getSelected();
   	   if (row) {
   		  if(row.pm10 =='暂无数据'){
  			mini.alert("该设备暂无数据", "提示", function callback(action){return;});
   		  }else{
   			 mini.open({
                 url: "${ctx}/device/data/toeContrJsp?device_guid="+row.device_guid ,
                 title: "控制设备",
                 height: 380,
                 width: 450,
                 onload: function () {
              	   var iframe = this.getIFrameEl();
   	   	           /* var data = { action: "editAddress", id: row.device_guid };
   	   	           iframe.contentWindow.SetData(data);  */
                 },
                   ondestroy: function (action) {
                   grid.reload();
                 }
             });
   		  }
   		  
   	   }else{
			mini.alert("请先选择设备", "提示", function callback(action){return;});
   	   }
   }
	
   
  	var isLeaf=false;
    var org_id;
    function onNodeSelect(e) {
       var tree = e.sender;
       var nowNode = e.node;
       var level = tree.getLevel(nowNode);
       var node = e.node;
       isLeaf = e.isLeaf;
       org_id=node.id;       	   
   	   grid.load({org_id : org_id}); 
   }
        
    function onBeforeExpand(e) {
        var tree = e.sender;
        var nowNode = e.node;
        var level = tree.getLevel(nowNode);
        var root = tree.getRootNode();
        tree.cascadeChild(root, function (node) {
            if (tree.isExpandedNode(node)) {
                var level2 = tree.getLevel(node);
                if (node != nowNode && !tree.isAncestor(node, nowNode)
                        && level == level2) {
                    tree.collapseNode(node, true);
                };
            };
        });
    }

     function search() {
 	    grid.load({
        	"remarks": $("#remarks").val(),
        	"device_guid": $("#device_guid").val(),
        	"org_id":org_id
        });
    } 
    
    function reset() {
   		 $("#remarks").val('');
    	 $("#device_guid").val('');
    	 grid.load({
           	"org_code":org_code
         });
    } 
    
</script>
</body>
</html>

