package com.service.rest;

import java.math.BigInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.service.controllers.UserService;
import com.utils.logging.MyLogger;

@Path("user")
public class UserWsApi {
	
	private static MyLogger logger = new MyLogger(UserWsApi.class);
	private UserService userService;
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String add (String paylaod)  {
		logger.logInfo("add user");
		return userService.add(paylaod);
	}
	
	@POST
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String update (String paylaod)  {
		logger.logInfo("update user");
		return userService.update(paylaod);
	}
	
	@GET
	@Path("get/{username}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String get (@PathParam("username") String username)  {
		logger.logInfo("get user");
		return userService.get(username);
	}
	
	@GET
	@Path("get/id/{id}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String get (@PathParam("id") BigInteger id)  {
		logger.logInfo("get user");
		return userService.get(id);
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String login (String request)  {
		logger.logInfo("login user");
		return userService.login(request);
	}
	
	
	@GET
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete (@PathParam("username") String username)  {
		logger.logInfo("delete user");
		return userService.delete(username);
	}

	public static MyLogger getLogger() {
		return logger;
	}

	public static void setLogger(MyLogger logger) {
		UserWsApi.logger = logger;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
}
