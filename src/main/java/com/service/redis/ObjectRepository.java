package com.service.redis;

import org.springframework.data.redis.core.RedisTemplate;


public class ObjectRepository {

	   private RedisTemplate<String, String> template;	
	 
	   public void add(String key, String value) {
	      template.opsForValue().set(key, value);
	   }
	 
	   public String getValue(String key) {
	      return template.opsForValue().get(key);
	   }
	 
	   public void delete(String key) {
	      template.opsForValue().getOperations().delete(key);
	   }

	   public RedisTemplate<String, String> getTemplate() {
		return template;
	   }

	   public void setTemplate(RedisTemplate<String, String> template) {
		this.template = template;
	   }
	   
	   
	   
}
