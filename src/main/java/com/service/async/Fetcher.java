package com.service.async;

import java.util.Map;

import com.service.models.User;
import com.utils.logging.MyLogger;

public class Fetcher {
	
		static MyLogger logger = new MyLogger(Fetcher.class);
		
		public synchronized static void fetch (User user){
			logger.logInfo("fetching photos for user id. "+user.get_id());
			Job job = null;
			 Map<String, Map<String, String>> services = user.getServices();
			 logger.logInfo("services.."+services);
			if (services.containsKey("facebook"))
			 job = new FacebookFetchJob();
			if (services.containsKey("instagram"))
			 job = new InstagramFetchJob();
			
			if (job == null) return;
			
			job.addContext("user", user);
			new Thread(job).start();
		}
		
		
}
