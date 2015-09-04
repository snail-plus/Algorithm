package com.whtriples.airPurge.util;

import java.security.MessageDigest;

public class Md5Util {
	
	public static String md5Pwd(String login_pwd)throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(login_pwd.getBytes()); 
		byte b[] = md.digest(); 
		int i; 

		StringBuffer buf = new StringBuffer(""); 
		for (int offset = 0; offset < b.length; offset++) { 
		i = b[offset]; 
		if(i<0) i+= 256; 
		if(i<16) 
		buf.append("0"); 
		buf.append(Integer.toHexString(i)); 
		} 
		return buf.toString();
	} 
	 
}
