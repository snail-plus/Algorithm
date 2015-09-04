package com.whtriples.airPurge.api.param;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestParam {
	
	public static void main(String[] args) throws JsonProcessingException, IOException {
		Param xmlParam = XmlParam.builder()
				.add("userGroup").attr("type", "work")
					.set("name", "group1")
					.set("size", 13)
					.add("list")
						.add("user")
							.set("name", "zhuyingxi")
							.set("age", "26")
							.add("friends")
								.set("user_id", "3")
								.set("user_id", "4")
							.end()
							.add("role")
								.set("name", "user")
							.end()
						.end()
						.add("user")
							.set("name", "liuwanzhen")
							.set("age", "30")
							.add("friends")
								.set("user_id", "1")
								.set("user_id", "2")
							.end()
							.add("role")
								.set("name", "admin")
							.end()
						.end()
					.end()
					.set("rule_id", "123")
				.end();
		
		Param jsonParam = JsonParam.builder()
				.set("name", "group1")
				.set("size", 13)
				.array("list")
					.add()
						.set("name", "zhuyingxi")
						.set("age", 26)
						.array("friends")
							.addValue("3")
							.addValue("4")
						.arrayEnd()
						.add("role")
							.set("name", "user")
						.end()
					.end()
					.add()
						.set("name", "liuwanzhen")
						.set("age", 30)
						.array("friends")
							.addValue(1)
							.addValue("2")
						.arrayEnd()
						.add("role")
							.set("name", "admin")
						.end()
					.end()
				.arrayEnd()
				.set("rule_id", "123")
				.add("role")
					.set("name", "admin")
				.end()
			.end();
						
						
		String json = jsonParam.toString(true);
		String xml = xmlParam.toString(true);
		System.out.println(json);
		System.out.println(xml);
		JsonNode rootNode = new ObjectMapper().readTree(json);
		System.out.println("test:" + rootNode.path("role1").path("name"));
		System.out.println("----");
		System.out.println(rootNode.getClass());
		System.out.println(rootNode.get("role.name"));
	}

}
