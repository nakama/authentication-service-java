package com.service.async;

import java.util.HashMap;
import java.util.Map;

public abstract class Job implements Runnable {
		
		private Map<String , Object> context = new HashMap<String,Object>();
		
		public void addContext(String key, Object v) {
			context.put(key, v);
		}
		
		public Map<String , Object> getContext (){
			return context;
		}
	
}
