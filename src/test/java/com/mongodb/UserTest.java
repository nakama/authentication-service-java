package com.mongodb;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import com.redis.BeanInit;
import com.service.controllers.UserService;
import com.utils.json.JSONMapper;
import com.utils.json.JSONMapperException;

public class UserTest extends BeanInit {

	public UserService userService;
	public Map<String,Object> data = new HashMap<String,Object>();
	
	public static void main (String[] m){
		UserTest.init();
		UserTest test = new UserTest();
		UserService service = (UserService) UserTest.getContext().getBean("userService");
		test.userService = service;
		String payload = test.getPayload();
		System.out.println(payload);
		test.validateAdd(test.add(payload));
		payload = test.getPayload();
		test.validateUpdate(test.update(payload));
		test.validateGet(test.get("marcelo"));
		test.validateLogin(test.login(payload));
		test.validateLogin(test.delete(payload));
	}
	
	public String getFacebook(String id){
		System.out.println("== GET USER BY SOCIAL ==");
		return userService.getFacebook("id",id);
	}
	public String list (){
		return userService.list();
	}
	
	public String add (String payload){
		System.out.println("== ADD USER ==");
		return userService.add(payload);
	}
	
	public void validateAdd (String result) {
		try {
			System.out.println(result);
			Map resp = (Map) JSONMapper.toObject(result, Map.class);
			Map object = (Map) resp.get("object");
			String id = (String) object.get("id");
			data.put("id", id);
		} catch (JSONMapperException e) {
			e.printStackTrace();
		}
			System.out.println(result);
	}
	
	public String update(String payload){
		System.out.println("== UPDATE USER ==");
		return userService.update(payload);
	}
	
	public void validateUpdate (String result){
		System.out.println(result);
	}
	
	public String get (String username){
		System.out.println("== GET USER ==");
		return userService.get(username);
	}
	
	public void validateGet (String result){
		System.out.println(result);
	}

	public String delete (String payload){
		System.out.println("== DELETE USER ==");
		return userService.delete(payload);
	}
	
	public void validateDelete (String result){
		System.out.println(result);
	}
	
	public String login (String payload){
		System.out.println("== LOGIN USER ==");
		return userService.login(payload);
	}
	
	public void validateLogin (String result){
		System.out.println(result);
	}

	public String getPayload(){
		data.put("username", "marcelo");
		Map profile = new HashMap<String,String>();
		profile.put("name", "marcelo oliveira");
		profile.put("email", "d2oliveira@gmail.com");
		profile.put("avatar", "http://someurl");
		data.put("profile", profile);
		data.put("password" , "marcelo");
		Map<String,Map<String,String>> services = new HashedMap();
		Map<String, String> facebook = new HashMap<String,String>();
		Map<String, String> instagram = new HashMap<String,String>();
		facebook.put("auth_token","1234");
		instagram.put("auth_token","1234");
		services.put("facebook", facebook);
		services.put("instagram", instagram);
		data.put("services",services);
		return JSONMapper.toString(data);
	}
}
