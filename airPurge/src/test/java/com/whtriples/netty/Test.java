package com.whtriples.netty;

import com.alibaba.fastjson.JSONObject;

public class Test {

	static void test(Integer a){
		a = 45;
	}
	
	public static void main(String[] args) {
		String str ="{\"reply\": \"control\",\"state\": \"ok\",\"msg\": \"87498fb23dee40a0a94acea618707a31_0_1,switch,ok\"}";
		Object parse = JSONObject.parse(str);
		System.out.println(parse.toString());
		String[] split = "34,3,4,5,6,".split(",");
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) {
			//System.out.println(StringUtils.isEmpty(split[i]));
		}
		System.out.println(String.copyValueOf(str.toCharArray()));
	}
}
