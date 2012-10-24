package com.service.redis;


public interface MessageSubscriber {

    void handleMessage(String message);
    
}
