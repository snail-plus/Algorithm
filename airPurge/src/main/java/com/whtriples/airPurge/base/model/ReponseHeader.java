package com.whtriples.airPurge.base.model;

import java.util.Random;

public class ReponseHeader extends BaseReponseHeader{

	private String level;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public static void main(String[] args) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 6; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    } 
	    
	    System.out.println(sb.toString());
	}
}
