<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<title>控制设备</title>
<%-- <jsp:include page="/meta.jsp" /> --%>
<style type="text/css">
	body{
      width: 100%;
      height: 100%;
      overflow:hidden; 
	}
	
  #qrform{
      height:750px;   
      line-height:750px;   
      overflow:hidden; 
      text-align: center;  
  }
  img{
      width:  250px;
      height: 250px;
  }
</style>
</head>
<body>
	<div  class="form">
	    <div id="qrform">
	        <img alt="设备二维码" src="${device_qr_url}">
	        <%-- <p>${device_name}</p> --%>
	    </div>
	</div>
    
	<script type="text/javascript">
	  setTimeout("QR.printQR()", 3000);
	  var QR = {
			  printQR : function() {
				  window.print();
			  }
	  };
	  
    </script>
</body>
</html>