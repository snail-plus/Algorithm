package com.whtriples.netty;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

class Client {

	public static void main(String args[]) throws Exception {  
	      //为了简单起见，所有的异常都直接往外抛  
	      String host = "127.0.0.1";  //要连接的服务端IP地址  
	      int port = 9998;   //要连接的服务端对应的监听端口  
	      //与服务端建立连接  
	      Socket client = new Socket(host, port); 
	     
	      Writer writer = null;
	      try {
	    	  client.setKeepAlive(true);
		      //建立连接后就可以往服务端写数据了  
	    	  writer = new OutputStreamWriter(client.getOutputStream());  
	    	  while(true){
			      writer.write("Hello Server.");  
			      writer.flush();//写完后要记得flush  
	    	  }
	    	  
		} catch (Exception e) {
			 System.out.println();
			e.printStackTrace();
			writer.close();
			client.close();
		}
	    
	   }  
	     
	}  

