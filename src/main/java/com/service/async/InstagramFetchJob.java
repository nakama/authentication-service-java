package com.service.async;

import java.util.Map;
import com.service.models.User;
import com.utils.InvalidConstantException;
import com.utils.logging.MyLogger;
import com.utils.wsutils.ClientCall;
import com.utils.wsutils.ServiceException;



public class InstagramFetchJob  extends Job {
	
	private MyLogger logger = new MyLogger(InstagramFetchJob.class);
	
	@Override
	public void run() {
		logger.logInfo("executing InstagramFetchJob");
		User user = (User) super.getContext().get("user");
		String userId = user.get_id();
		Map<String,String> facebook = user.getServices().get("instagram");
		String accessToken = facebook.get("auth_token");
		String endpoint;
		try {
			endpoint = com.utils.Constants.get("photoServiceEndpoint") + com.utils.Constants.get("instagramFetchURI") + userId +"/"+ accessToken;
			logger.logInfo("calling endpoint : "+endpoint);
			ClientCall.get(endpoint);
		} catch (InvalidConstantException e) {
			logger.logError(e.getMessage(),e);
		} catch (ServiceException e) {
			logger.logError(e.getMessage(), e);
		}
	}
	
}
