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
	
	@GET
	@Path("get/facebook/id/{id}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getFacebook (@PathParam("id") String id)  {
		logger.logInfo("get user by facebook");
		return userService.getFacebook("id",id);
	}
	
	@GET
	@Path("get/instagram/id/{id}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getInstagram (@PathParam("id") String id)  {
		logger.logInfo("get user by instangram ");
		return userService.getInstagram("id", id);
	}
	
	@GET
	@Path("get/facebook/token/{token}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getFacebookToken (@PathParam("token") String token)  {
		logger.logInfo("get user by facebook token");
		return userService.getFacebook("auth_token", token);
	}
	
	@GET
	@Path("get/instagram/token/{token}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getInstagramToken (@PathParam("token") String token)  {
		logger.logInfo("get user by instangram ");
		return userService.getInstagram("auth_token", token);
	}
	
	
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login (String request)  {
		logger.logInfo("login user");
		return userService.login(request);
	}
	
	@POST
	@Path("login/facebook")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String loginFacebook (String request)  {
		logger.logInfo("login Facebook user");
		return userService.loginFacebook(request);
	}
	
	@POST
	@Path("login/instagram")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String loginInstagram (String request)  {
		logger.logInfo("login Instagram user");
		return userService.loginInstagram(request);
	}
	
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String delete (String payload)  {
		logger.logInfo("delete user");
		return userService.delete(payload);
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
