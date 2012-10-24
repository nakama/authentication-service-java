package com.service.rest;

import com.utils.wsutils.ServiceException;

public class APIKeyValidator {

	public void validate(String key) throws ServiceException {
		if (key == null) throw new ServiceException("invalid key");
	}
	
}
