package com.service.async;


public abstract class Action implements Runnable {
	
	public String request;
	public boolean hasCallback;
	public Callback callback;
	
	
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public boolean isHasCallback() {
		return hasCallback;
	}
	public void setHasCallback(boolean hasCallback) {
		this.hasCallback = hasCallback;
	}
	public Callback getCallback() {
		return callback;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	
	
	
}
