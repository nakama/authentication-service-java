package com.mongodb;

import java.util.HashMap;
import java.util.Map;

import com.utils.json.JSONMapper;

public class UserTest {

	public static void main (String[] m){
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", "d2marcelo");
		map.put("name", "marcelo oliveira");
		map.put("email", "d2oliveira@gmail.com");
		map.put("password" , "marcelo");
		System.out.println(JSONMapper.toString(map));
	}
}
