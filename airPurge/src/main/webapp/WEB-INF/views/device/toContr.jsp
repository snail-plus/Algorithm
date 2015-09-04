<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<title>控制设备</title>
<%-- <jsp:include page="/meta.jsp" /> --%>
<style type="text/css">
  .tab{
   margin: 0 auto;
   text-algin: center;
  }
</style>
<script type="text/javascript" src="${js}/angular.min.js"></script>
<script type="text/javascript" src="${js}/sockjs-0.3.4.min.js"></script>
</head>
<body ng-app="myApp" style="width: 96%; height: 96%;">
	<div id="editform" class="form" ng-controller="dataCtrl">
		<table class="tab">
		   <tr>
		     <td>温度：</td>
		     <td><span>{{device.temp}}</span>
		   <tr>
		   <tr>
		     <td>湿度：</td>
		     <td><span>{{device.hum}}</span>
		   <tr>
		   <tr>
		     <td>pm2.5：</td>
		     <td><span>{{device.pm25}}</span>
		   <tr>
		   <tr>
		     <td>pm10：</td>
		     <td><span>{{device.pm10}}</span>
		   <tr>
		   <tr>
		     <td>设备档位</td>
		     <td><span>{{device.gear}}</span>
		   <tr>
		   <tr>
		     <td>设备运行状态：</td>
		     <td><span>{{device.device_mode}}</span>
		   <tr>
		   <tr>
		     <td>设备所属城市：</td>
		     <td><span>{{device.cityName}}</span>
		   <tr>
		</table>
		<div style="text-align: center; padding: 10px;">
			<button ng-click="controll_on_off()" style="width: 60px; margin-right: 5px;">开关机</button> 
			<button ng-click="controll_mode()" style="width: 60px;margin-right: 5px;">模式</button>
			<button ng-click="controll_gear(0)" style="width: 60px;margin-right: 5px;" >档位1</button>
			<button ng-click="controll_gear(1)" style="width: 60px;margin-right: 5px;">档位2</button>
			<button ng-click="controll_gear(2)" style="width: 60px;margin-right: 5px;">档位3</button>
		</div>
	</div>
    
	<script type="text/javascript">
    var device_guid = '${device_guid}';
    var user_id = '${user_id}';
    var token = '${token}';
    var serverSocketUrl = "${ctx}/ws";
	var app = angular.module('myApp', []);
	
	app.controller('dataCtrl', function($scope,$timeout){
		$scope.device = {};
		$scope.socket = {};
		$scope.device.temp = "32℃"; //温度
		$scope.device.hum = "40%"; //湿度
		$scope.device.pm25 = 34; //pm2.5值
		$scope.device.pm10 = 56;
		$scope.ret = {};
		$scope.ret.cmd  = 'connect';
		$scope.ret.device_guid = device_guid;
		$scope.ret.user_id = user_id;
		$scope.ret.token = token;
		var cmd_type = "";
	    
		//发送控制开关的参数
	    $scope.toggleOnOFFSendMsg = function(){
			//关闭打开风机
			var on_or_off = parseInt($scope.device.status)>0?'0':'1'; 
			console.log("机器要操作状态:"+on_or_off);
			$scope.socket.cmd = 'control'
			cmd_type = "switch";
			$scope.socket.data = [device_guid,cmd_type,on_or_off];
		}
		
	   //发送手动自动开关的参数
	    $scope.toggleModeMsg = function(){
			//要操作的档位模式 0代表自动档位 1代表手动档位
			console.log("crtlMode :"+$scope.device.ctrlmode);
			var auto_or_hand = parseInt($scope.device.ctrlmode)>0?'0':'1'; 
			console.log("机器要调整档位模式:"+(auto_or_hand=='0'?'自动':'手动'));
			$scope.socket.cmd = 'control';
			cmd_type = "ctrlmode"; //手动或者自动模式
			$scope.socket.data = [device_guid,cmd_type,auto_or_hand];
		}
		
		//发送调整风机档位参数
	    $scope.toggleGearMsg = function (gear){
	        if($scope.device.ctrlmode =='0'){
				 $scope.controll_mode();//自动档位的时候转换为手动档位
			}
			console.log("机器目前档位:"+$scope.device.gear);
			console.log("机器期望调整档位:"+gear);
			$scope.socket.cmd = 'control'
			cmd_type = "gear"; //手动或者自动模式
			$scope.socket.data = [device_guid,cmd_type,gear];
		}
		
	        $scope.sock = null;
			$scope.sock = new SockJS(serverSocketUrl);
			$scope.sock.onopen = function() {
			console.log('连接消息:'+JSON.stringify($scope.ret))
			$scope.sock.send(JSON.stringify($scope.ret)); 
		    $scope.controll_on_off = function (){
		    	alert('aaaaaaa');
			   $scope.toggleOnOFFSendMsg();
			   $scope.sock.send(JSON.stringify($scope.socket));
			   console.info(JSON.stringify($scope.socket));
			}
			$scope.controll_mode = function(){
				$scope.isActive = val;
				$scope.toggleModeMsg();
			    $scope.sock.send(JSON.stringify($scope.socket));
			}
			$scope.controll_gear = function(val){
				$scope.isActive = val;
				$scope.toggleGearMsg(val);
				$scope.sock.send(JSON.stringify($scope.socket));
			}
		};
		
		$scope.sock.onmessage = function (event) {
			console.log('WebSocket 接收消息------start');
			console.log("机器实时开启状态:"+(JSON.parse(event.data).status=='0'?'关闭':'开启'));
			console.log("机器实时模式:"+(JSON.parse(event.data).ctrlmode=='0'?'自动':'手动'));
			var data = JSON.parse(event.data);
			console.info(data);
		    $scope.device.device_mode = "";
		    $scope.device.ctrlmode=data.cityName;
			$scope.$apply(function () {
			 　	$scope.device.ctrlmode = data.ctrlmode;
				$scope.device.status = data.status;
				$scope.device.gear = data.gear;
				$scope.device.pm10 = data.pm10;
				if(data.cityName!=null&&data.cityName!='' &&data.aqi!=null &&data.aqi){
					$scope.device.cityName = data.cityName;
					$scope.device.aqi = data.aqi;
				}
				$scope.device.pm25 = data.pm25; //pm2.5值
				if(data.status=='0'){
					$scope.device.device_mode = "关闭";
				}else{
					$scope.device.device_mode = (data.ctrlmode=='0'?'自动模式':'手动模式'); //风机的模式
				}
				$scope.device.temp = data.temp+"℃"; //温度
				$scope.device.hum = data.hum+"%"; //湿度
			});
			console.log("机器实时档位:"+(data.gear));
			console.log("rece:"+$scope.device.status)
	  	};
	  	
	   $scope.sock.onclose = function(){
		   console.log('WebSocket 关闭-----关闭！');
	   }
	});
	
    //取消
    function onCancel(e) {
        CloseWindow("cancel");
    }

    //关闭窗口方法
    function CloseWindow(action) {
        window.close();
    }
        
</script>
</body>
<html>