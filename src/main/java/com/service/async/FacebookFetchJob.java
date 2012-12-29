package com.service.async;

import java.util.Map;

import com.service.models.User;
import com.utils.InvalidConstantException;
import com.utils.logging.MyLogger;
import com.utils.wsutils.ClientCall;
import com.utils.wsutils.ServiceException;



public class FacebookFetchJob  extends Job {
	
	private MyLogger logger = new MyLogger(FacebookFetchJob.class);
	
	@Override
	public void run() {
		logger.logInfo("executing FacebookFetchJob");
		User user = (User) super.getContext().get("user");
		String userId = user.get_id();
		Map<String,String> facebook = user.getServices().get("facebook");
		String accessToken = facebook.get("auth_token");
		String endpoint;
		try {
			endpoint = com.utils.Constants.get("photoServiceEndpoint") + com.utils.Constants.get("facebookFetchURI") + userId +"/"+ accessToken;
			logger.logInfo("calling endpoint : "+endpoint);
			ClientCall.get(endpoint);
		} catch (InvalidConstantException e) {
			logger.logError(e.getMessage(),e);
		} catch (ServiceException e) {
			logger.logError(e.getMessage(), e);
		}
	}
	
}
