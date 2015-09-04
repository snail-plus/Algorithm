mini_debugger =false;

var myalert = window.alert ;

var myTimeout = window.setTimeout ;

//try {delete window.setTimeout}catch(e){}
//try {delete window.execScript}catch(e){}


window.alert = function(str){
	if(str){
		if(typeof str=="string"&&str.indexOf("miniui") != -1){
			return ;
		}
	}
	myalert(str) ;
		
}

/*window.setTimeout = function(){
	 
}*/