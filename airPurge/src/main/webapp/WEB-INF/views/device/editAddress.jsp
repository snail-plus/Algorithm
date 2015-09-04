<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<title>编辑设备地址</title>
<jsp:include page="/meta.jsp" />
<script src="http://api.map.baidu.com/api?v=2.0&ak=d1L9YY7ID9GCFLTXw8Di1Ieg" type="text/javascript"></script>
</head>
<body style="width: 96%; height: 96%;">
	<div id="editform" class="form">
	    <input class="mini-hidden" name="lng" id="c_lng"> 
	    <input class="mini-hidden" name="lat" id="c_lat"> 
		<input class="mini-hidden" name="device_id">
		<table class="tab">
            <tr>
				<td class="tab_td" align="right" width="90px;">本地服务器地址:</td>
				<td class="tab_td"><input vtype="maxLength:64"  minLengthErrorText="本地服务器地址不能多余于64个字符"
					style="width: 300px;" name="city_name" required="true"
					class="mini-textbox" id="c_address" onblur = "changeCAddress"/></td>
			</tr>
			<tr>
				<td class="tab_td" align="right" width="90px;">本地服务器坐标:</td>
				<td class="tab_td" align="center" height="300px;">
					<div id="map1" style="width: 300px; height: 290px;"></div>
				</td>
			</tr>
		</table>
		<div style="text-align: center; padding: 10px;">
			<a class="mini-button" onclick="onOk" id="okBtn" style="width: 60px; margin-right: 20px;">保存</a> 
			<a class="mini-button" onclick="onCancel" style="width: 60px;">取消</a>
		</div>
	</div>

	<script type="text/javascript">
    mini.parse();
    var editform = new mini.Form("editform");
    var map = new BMap.Map("map1");
    var k=mini.get("c_lng").getValue();
    var b=mini.get("c_lat").getValue();
    map.enableScrollWheelZoom(true);
    if(k!=''&&b!=''){
    	 map.centerAndZoom(new BMap.Point(k,b),14);
    	 var marker = new BMap.Marker(new BMap.Point(k, b));  // 创建标注，为要查询的地址对应的经纬度
	     map.addOverlay(marker);
    }else{
    	map.centerAndZoom(new BMap.Point(114.31,30.52),14);
    }
    map.addEventListener("click", showMarker);
    
    function SetData(data){
    	if(data.action == "editAddress"){
    		data = mini.clone(data);
    		$.post("${ctx}/device/mang/getDeviceById/"+data.id,function(text){
    			  var o = mini.decode(text);
    			  editform.setData(o);
    			  map.centerAndZoom(new BMap.Point(o.lng, o.lat), 14);
			      var marker = new BMap.Marker(new BMap.Point(o.lng, o.lat));  // 创建标注，为要查询的地址对应的经纬度
			      map.addOverlay(marker);
    		});
    	}
    }
    
    
    function showMarker(e){
		 map.clearOverlays();
		 var k=e.point.lng; 
		 var b=e.point.lat;
		 mini.get("c_lng").setValue(k);
         mini.get("c_lat").setValue(b);
	     map.centerAndZoom(new BMap.Point(k, b), 14);
	     var marker = new BMap.Marker(new BMap.Point(k, b));  // 创建标注，为要查询的地址对应的经纬度
	     map.addOverlay(marker); 
	}
	
	var localSearch = new BMap.LocalSearch(map);
	 function changeCAddress(){
	    	var address = mini.get("c_address").getValue();
	    	if(address){
	    		localSearch.setSearchCompleteCallback(function (searchResult) {
	    		      var poi = searchResult.getPoi(0);
	                  //alert(poi.point.lng+","+poi.point.lat); //获取经度和纬度，将结果显示在文本框中
	                   if(poi!=undefined){
	                	   var k=poi.point.lng;
	                       var b=poi.point.lat;
	                       mini.get("c_lng").setValue(k);
	    		           mini.get("c_lat").setValue(b);
	        		       map.centerAndZoom(poi.point, 14);
	        		       var marker = new BMap.Marker(new BMap.Point(k, b));  // 创建标注，为要查询的地址对应的经纬度
	        		       map.clearOverlays();
	        		       map.addOverlay(marker);  
	        		       console.info(poi);
	                   }else{
	                	   notify("您输入的地址不存在请重新输入");
	                   }
	    	      });
	    	     localSearch.search(address);
	    	}
	    }
	 
	//确定
    function onOk(e) {
        editform.validate();
        if (editform.isValid() == true){
        	var data = editform.getData();
        	 $.post("${ctx}/device/mang/updateAddress",data,function (msg) {
           	  if (msg) {
                   notify("编辑成功");
                   CloseWindow();
                }else{
                	notify("编辑失败");
                	CloseWindow();
           	   }
            });
        }
    }
 

    //取消
    function onCancel(e) {
        CloseWindow("cancel");
    }

    //关闭窗口方法
    function CloseWindow(action) {
        if (action == "close" && editform.isChanged()) {
			   mini.alert("请保存数据", "提示", function callback(action){return;});
        }
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }
        
</script>
</body>
<html>