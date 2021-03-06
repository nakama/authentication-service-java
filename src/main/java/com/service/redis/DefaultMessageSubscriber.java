package com.service.redis;


import java.util.ArrayList;
import java.util.List;
import com.service.async.Action;
import com.service.async.DefaultThreadPoolExecutor;
import com.utils.logging.MyLogger;


public class DefaultMessageSubscriber implements MessageSubscriber {

	private List<Action> actions = new ArrayList<Action>();
	private MyLogger logger = new MyLogger(DefaultMessageSubscriber.class);
	private DefaultThreadPoolExecutor es = new DefaultThreadPoolExecutor();
	
	@Override
	public void handleMessage(String message) {
		try {
			for (Action action: actions) {
					es.execute(action);
			}
			es.waitForExecuted();
			} catch (Exception e) {
				es.shutdown();
				logger.logError("Unexpected exception executing action", e);
			}
		
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}
