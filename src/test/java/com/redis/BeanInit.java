package com.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanInit {

		protected static ApplicationContext sContext;
	     
	    public static ApplicationContext getContext() {
	    	if (sContext == null) {
				init();
			}
			return sContext;
	    }
	    
	  	
	public static void init()  {
		try {
			String[] configLocations = {"classpath:/spring/mongodb-service-dao.xml","classpath:/spring/service-beans.xml"};
			
			sContext = new ClassPathXmlApplicationContext(configLocations);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
