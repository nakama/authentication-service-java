package com.service.async;

import com.utils.logging.MyLogger;

public class UserAction extends Action {

	private MyLogger logger = new MyLogger();
	
	@Override
	public void run() {
		String request = super.getRequest();
		logger.logInfo("my request: "+request);
		logger.logInfo("has callback: "+ super.hasCallback);
		logger.logInfo("callback obj : "+ super.getCallback());
		
	}

	
	
}
