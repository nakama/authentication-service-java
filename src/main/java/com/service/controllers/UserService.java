package com.service.controllers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import com.service.dao.UserDao;
import com.service.models.Profile;
import com.service.models.User;
import com.utils.json.JSONMapper;
import com.utils.json.JSONMapperException;
import com.utils.wsutils.ServiceException;
import com.utils.wsutils.ServiceResponse;

public class UserService {

	private UserDao userDao;
	
	public String login (String request){
	try {
		Map<String,Object> req = (Map<String, Object>) JSONMapper.toObject(request, Map.class);
		// get username and password
		String username =(String) req.get("username");
		String password =(String) req.get("password");
		// fetch user using username
		User user = userDao.get(username);
		if (user == null) return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
		// check if password matches
		if (this.hasPasswordChanged(password, user.getPassword()))
			return ServiceResponse.getSingleObjectResponse(false, "password does not match", null);
		else  {
			// user found.. create object
			return ServiceResponse.getSingleObjectResponse(true, "login success", buildUser(user));
		}
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
		}
	}
	
	public String loginFacebook(String request) {
		try {
			Map<String,Object> req = (Map<String, Object>) JSONMapper.toObject(request, Map.class);
			if (req == null) return ServiceResponse.getSingleObjectResponse(false, "invalid object structure", null);
			User usr = buildUser(req);
			if (usr == null) return ServiceResponse.getSingleObjectResponse(false, "user object not found", null);
			Map<String, Map<String, String>> serviceMap  = usr.getServices();
			if (serviceMap == null) return ServiceResponse.getSingleObjectResponse(false, "service object not found", null);
			Map<String,String> instagram = serviceMap.get("facebook");
			if (instagram == null) return ServiceResponse.getSingleObjectResponse(false, "facebook object not found", null);
			String auth_token =(String) instagram.get("auth_token");
			if (auth_token == null) return ServiceResponse.getSingleObjectResponse(false, "invalid auth_token", null);
			String id =(String) instagram.get("id");
			if (id == null) return ServiceResponse.getSingleObjectResponse(false, "invalid id", null);
			String username =(String) usr.getUsername();
			if (username == null) return ServiceResponse.getSingleObjectResponse(false, "invalid username", null);
			User user = userDao.getBySocial("facebook", "id", id);
			if (user == null) return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			// check if token matches
			if (user.getUsername().equals(username)) {
				Map<String, Map<String, String>> services = user.getServices();
				Map<String,String> map = services.get("facebook");
				map.put("auth_token", auth_token);
				services.put("facebook", map);
				user.setServices(services);
				userDao.update(user);
				return ServiceResponse.getSingleObjectResponse(true, "login success", buildUser(user));
			}
			else  
				return ServiceResponse.getSingleObjectResponse(false, "invalid id or token", null);
			} catch (ServiceException e) {
				return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
			} catch (JSONMapperException e) {
				return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
			}

	}	
	public String loginInstagram(String request) {
			try {
				Map<String,Object> req = (Map<String, Object>) JSONMapper.toObject(request, Map.class);
				if (req == null) return ServiceResponse.getSingleObjectResponse(false, "invalid object structure", null);
				User usr = buildUser(req);
				if (usr == null) return ServiceResponse.getSingleObjectResponse(false, "user object not found", null);
				Map<String, Map<String, String>> serviceMap  = usr.getServices();
				if (serviceMap == null) return ServiceResponse.getSingleObjectResponse(false, "service object not found", null);
				Map<String,String> instagram = serviceMap.get("instagram");
				if (instagram == null) return ServiceResponse.getSingleObjectResponse(false, "instagram object not found", null);
				String auth_token =(String) instagram.get("auth_token");
				if (auth_token == null) return ServiceResponse.getSingleObjectResponse(false, "invalid auth_token", null);
				String id =(String) instagram.get("id");
				if (id == null) return ServiceResponse.getSingleObjectResponse(false, "invalid id", null);
				String username =(String) usr.getUsername();
				if (username == null) return ServiceResponse.getSingleObjectResponse(false, "invalid username", null);
				User user = userDao.getBySocial("instagram", "id", id);
				if (user == null) return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
				// check if token matches
				if (user.getUsername().equals(username)) {
					Map<String, Map<String, String>> services = user.getServices();
					Map<String,String> map = services.get("instagram");
					map.put("auth_token", auth_token);
					services.put("instagram", map);
					user.setServices(services);
					userDao.update(user);
					return ServiceResponse.getSingleObjectResponse(true, "login success", buildUser(user));
				}
				else  
					return ServiceResponse.getSingleObjectResponse(false, "invalid id or token", null);
				} catch (ServiceException e) {
					return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
				} catch (JSONMapperException e) {
					return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
				}
		
	}
	
	public String list () {
		List<User> list;
		try {
			list = userDao.list();
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, e.getMessage()); 
		}
		return ServiceResponse.getSingleObjectResponse(true, "success", list);
	}
	public String add (String request) {
		try {
			Map<String,Object> req = (Map<String, Object>) JSONMapper.toObject(request, Map.class);
			// build user object from payload
			User user = buildUser(req);
			validateUser(user);
			// is username already in use? if so, return failure
			User userAlreadyCreated = userDao.get(user.getUsername());
			if (userAlreadyCreated != null)  
				return ServiceResponse.getSafeServiceResponse(false, "username already in use");
			// hash password
			String hashedPWD = hashPassword(user.getPassword());
			user.setPassword(hashedPWD);
			userDao.add(user);
			Map resp = buildUser(user);
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request. "+e.getMessage());
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request" +e.getMessage());
		}
	}
	
	public String update (String request) {
		try {
			Map<String,Object> req = (Map<String, Object>) JSONMapper.toObject(request, Map.class);
			// build user object from payload
			User updateUser = buildUser(req);
			BigInteger id = updateUser.getId();
			if (id == null) return ServiceResponse.getSafeServiceResponse(false, "invalid id");
			User user = userDao.get(id);
			if (user == null) return ServiceResponse.getSafeServiceResponse(false, "user not found");
			Map updateUsr = buildUser(updateUser);
			updateUserObj(user,req);
			userDao.update(user);
			user = userDao.get(user.getId());
			Map resp = buildUser(user);
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
			BigInteger id = new BigInteger(req.get("id"));
			User user = userDao.get(id);
			if (user == null) return ServiceResponse.getSafeServiceResponse(false, "user not found");
			userDao.delete(user);
			return ServiceResponse.getSafeServiceResponse(true, "success");
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request" +e.getMessage());
		} catch (JSONMapperException e) {
			return ServiceResponse.getSafeServiceResponse(false, "invalid request" +e.getMessage());
		}
	}
	
	public String get (String username) {
		try {
			User user = userDao.get(username);
			if (user == null) 
				return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			Map resp = buildUser(user);
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "user not found");
		} 
	}
	
	public String get (BigInteger id) {
		try {
			User user = userDao.get(id);
			if (user == null) 
				return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			Map resp = buildUser(user);
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "user not found");
		} 
	}
	
	public String getFacebook (String key, String value) {
		try {
			User user = userDao.getBySocial("facebook",key, value);
			if (user == null) 
				return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
			Map resp = buildUser(user);
			return ServiceResponse.getSingleObjectResponse(true, "success", resp);
		} catch (ServiceException e) {
			return ServiceResponse.getSafeServiceResponse(false, "user not found");
		} 
	}
	
		public String getInstagram (String key, String value) {
			try {
				User user = userDao.getBySocial("instagram",key, value);
				if (user == null) 
					return ServiceResponse.getSingleObjectResponse(false, "user not found", null);
				Map resp = buildUser(user);
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
	
	private User buildUser (Map req) throws JSONMapperException{
		Map profileMap = (Map) req.get("profile");
		String name =(String) profileMap.get("name");
		String email =(String)profileMap.get("email");
		String avatar =(String)profileMap.get("avatar");
		String username =(String)req.get("username");
		String password =(String)req.get("password");
		Map services =(Map) req.get("services");
		String id =(String)req.get("id");
		BigInteger usrId = null;
		if (id != null)
			usrId = new BigInteger(id);
		
		User user = new User();
		Profile profile = new Profile();
		profile.setEmail(email);
		profile.setName(name);
		profile.setAvatar(avatar);
		user.setProfile(profile);
		user.setPassword(password);
		user.setUsername(username);
		user.setServices(services);
		user.setId(usrId);
		return user;
		
		
	}
	private void validateUser (User user) throws ServiceException{
		Profile profile = user.getProfile();
		if (profile == null) throw new ServiceException ("profile is required");
		if (profile.getEmail() == null) throw new ServiceException ("email is required");
		if (profile.getName() == null) throw new ServiceException ("name is required");
		if (user.getPassword() == null) throw new ServiceException ("password is required");
		if (user.getUsername() == null) throw new ServiceException ("username is required");
	}
	
	private void updateUserObj (User user, Map<String, Object> map) throws JSONMapperException{
		String password = (String) map.get("password");
		if (password != null) {
			if (this.hasPasswordChanged(password, user.getPassword())){
				String hashedPWD = hashPassword(user.getPassword());
				user.setPassword(hashedPWD);
			}
		}
		Map profileMap = (Map) map.get("profile");
		Profile profile = user.getProfile();
		if (profileMap != null){
			String name = (String) profileMap.get("name");
			if (name != null ) profile.setName(name);
			String email = (String) profileMap.get("email");
			if (email != null) profile.setEmail(email);
			String avatar = (String) profileMap.get("avatar");
			if (avatar != null) profile.setAvatar(avatar);
			
		}
		user.setProfile(profile);
		Map<String, Map<String, String>> servicesMap = user.getServices();
		Map<String, Map> services = (Map) map.get("services");
		if (services != null){
			for (Map.Entry<String, Map> entry : services.entrySet()) {
			    String key = entry.getKey();
			    Map<String,String> value = entry.getValue();
			    servicesMap.put(key, value);
			}
		}
		user.setServices(servicesMap);
	}
	
	
	
	private Map buildUser (User user){
		Map<String,Object> resp = new HashMap<String,Object>();
		Profile profile = user.getProfile();
		Map profileMap = new HashMap<String,String>();
		profileMap.put("name", profile.getName());
		profileMap.put("email", profile.getEmail());
		profileMap.put("avatar", profile.getAvatar());
		resp.put("username", user.getUsername());
		resp.put("profile", profileMap);
		resp.put("id", user.getId().toString());
		resp.put("services",user.getServices());
		return resp;
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
