package com.service.controllers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import com.service.dao.UserDao;
import com.service.models.User;
import com.utils.json.JSONMapper;
import com.utils.json.JSONMapperException;
import com.utils.wsutils.ServiceException;
import com.utils.wsutils.ServiceResponse;

public class UserService {

	private UserDao userDao;
	
	public String login (String request){
	try {
		Map<String,String> req = (Map<String, String>) JSONMapper.toObject(request, Map.class);
		String username =req.get("username");
		String password =req.get("password");
		User user = userDao.get(username);
		if (user == null) return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("name", user.getName());
		resp.put("email", user.getEmail());
		resp.put("id", user.getId().toString());
		resp.put("token", UUID.randomUUID().toString());
		if (this.hasPasswordChanged(password, user.getPassword()))
			return ServiceResponse.getSingleObjectResponse(false, "password not match", resp);
		else 
			return ServiceResponse.getSingleObjectResponse(true, "login success", resp);
		
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
		}
	}
	
	public String add (String request) {
		try {
			Map<String,String> req = (Map<String, String>) JSONMapper.toObject(request, Map.class);
			String name =req.get("name");
			String email =req.get("email");
			String username =req.get("username");
			String password =req.get("password");
			
			User user = new User();
			String hashedPWD = hashPassword(password);
			user.setUsername(username);
			user.setPassword(hashedPWD);
			user.setEmail(email);
			user.setName(name);
			userDao.add(user);
			Map<String,String> resp = new HashMap<String,String>();
			resp.put("name", user.getName());
			resp.put("email", user.getEmail());
			resp.put("id", user.getId().toString());
			resp.put("token", UUID.randomUUID().toString());
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "failed deleting user");
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request");
		}
	}
	
	public String update (String request) {
		try {
			Map<String,String> req = (Map<String, String>) JSONMapper.toObject(request, Map.class);
			String username = req.get("username");
			String password = req.get("password");
			String token = req.get("token");
			String id = req.get("id");
			if (token == null) return ServiceResponse.getSafeServiceResponse(false, "invalid token");
			if (id == null) return ServiceResponse.getSafeServiceResponse(false, "invalid id");
			User user = userDao.get(username);
			if (user == null) return ServiceResponse.getSafeServiceResponse(false, "user not found");
			if (password != null) {
			if (this.hasPasswordChanged(password, user.getPassword())){
				req.put("password", hashPassword(password));
			}
			}
			userDao.update(username, req);
			Map<String,String> resp = new HashMap<String,String>();
			resp.put("name", user.getName());
			resp.put("email", user.getEmail());
			resp.put("id", user.getId().toString());
			resp.put("token", UUID.randomUUID().toString());
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "failed deleting user");
		} catch (JSONMapperException e) {
			e.printStackTrace();
			return ServiceResponse.getSafeServiceResponse(false, "invalid request");
		}
	}
	
	public String delete (String request) {
		try {
			Map<String,String> req = (Map<String, String>) JSONMapper.toObject(request, Map.class);
			String idStr = req.get("id");
			if (idStr == null) return ServiceResponse.getSingleObjectResponse(false, "id not found", null);
			BigInteger id = BigInteger.valueOf(Long.parseLong(req.get("id")));
			String token = req.get("token");
			if (token == null) return ServiceResponse.getSingleObjectResponse(false, "token not found", null);
			User user = userDao.get(id);
			if (user == null) return ServiceResponse.getSafeServiceResponse(false, "user not found");
			userDao.delete(user);
			return ServiceResponse.getSafeServiceResponse(true, "success");
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "failed deleting user");
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request");
		}
	}
	
	public String get (String username) {
		try {
			User user = userDao.get(username);
			Map<String,String> resp = new HashMap<String,String>();
			if (user == null) 
				return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			resp.put("email", user.getEmail());
			resp.put("username", user.getUsername());
			resp.put("name", user.getName());
			resp.put("id", user.getId().toString());
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "user not found");
		} 
	}
	
	public String get (BigInteger id) {
		try {
			User user = userDao.get(id);
			Map<String,String> resp = new HashMap<String,String>();
			if (user == null) 
				return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			resp.put("email", user.getEmail());
			resp.put("username", user.getUsername());
			resp.put("name", user.getName());
			resp.put("id", user.getId().toString());
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "user not found");
		} 
	}
	
	private String hashPassword (String password) {
		// Hash a password for the first time
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return hashed;
		// gensalt's log_rounds parameter determines the complexity
		// the work factor is 2**log_rounds, and the default is 10
		//String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
	}
	
	
	private boolean hasPasswordChanged (String password, String previousHashed){
				if (BCrypt.checkpw(password, previousHashed))
					return false;
				else
					return true;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	
}
