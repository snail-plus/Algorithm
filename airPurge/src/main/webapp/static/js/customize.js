function configCustomize(app,param) {
    configHttp(app);
    configBackAndForward(app,param);
}

function configHttp(app) {
    app.config(function($httpProvider) {
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

        // Override $http service's default transformRequest
        $httpProvider.defaults.transformRequest = [function(data) {
            var param = function(obj) {
                var query = '';
                var name, value, fullSubName, subName, subValue, innerObj, i;

                for (name in obj) {
                    value = obj[name];

                    if (value instanceof Array) {
                        for (i = 0; i < value.length; ++i) {
                            subValue = value[i];
                            fullSubName = name + '[' + i + ']';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    } else if (value instanceof Object) {
                        for (subName in value) {
                            subValue = value[subName];
                            fullSubName = name + '[' + subName + ']';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    } else if (value !== undefined && value !== null) {
                        query += encodeURIComponent(name) + '='
                        + encodeURIComponent(value) + '&';
                    }
                }

                return query.length ? query.substr(0, query.length - 1) : query;
            };

            return angular.isObject(data) && String(data) !== '[object File]'
                ? param(data)
                : data;
        }];
    });
}

function configBackAndForward(app,param) {
    app.run(function($rootScope, $route, $location,$http){

        $rootScope.$on('$locationChangeSuccess', function() {
            $rootScope.actualLocation = $location.path();
        });

        $rootScope.$watch(function () {return $location.path()}, function (newLocation, oldLocation) {
            if($rootScope.actualLocation === newLocation) {
//                $scope.showAlertMsg('back or forward');
            	if($rootScope.loopSubmitConFirm){
            		window.clearInterval($rootScope.loopSubmitConFirm);
            	}
            }
            if(oldLocation == '/confirm'&&newLocation!="/success"){
            	$http.post('/freelyOrder/freelyorder/order/unlock',{token: param.token,}).
		  		success(function(data){
		  			if(data.errorCode == "0000"){
		  				$rootScope.isLockOrder=false;
					}else if(msg.errorCode=='0007'){
		        		$scope.showAlertMsg(data.errorMsg,function(){
		    	  			location.href=param.ctx+'/html5/in/'+param.restId+'/'+param.tableNo;
		        		});
			  		}else{
		        		$scope.showAlertMsg(data.errorMsg,function(){
		    	  			location.href=param.ctx+'/html5/in/'+param.restId+'/'+param.tableNo;
		        		});
			  		};
		  		}).
		  		error(function(x,t,m){
					location.href = param.ctx+"/html5/error/1";
					$rootScope.isLockOrder=false;
		  		});
            };
        });

    });
}

function getTimestamp() {
    var d = new Date();
    var y = d.getFullYear(),
        M = d.getMonth() + 1,
        D = d.getDate(),
        h = d.getHours(),
        m = d.getMinutes(),
        s = d.getSeconds(),
        pad = function (x) {
            x = x+'';
            if (x.length === 1) {
                return '0' + x;
            }
            return x;
        };
    return y + pad(M) + pad(D) + pad(h) + pad(m) + pad(s);
}