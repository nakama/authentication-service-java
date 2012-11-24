package com.service.dao;

import java.math.BigInteger;
import java.util.Map;

import com.service.models.User;
import com.utils.wsutils.ServiceException;

public interface UserDao {
	
	public User login (String username, String password) throws ServiceException;
	public void add (Object object) throws ServiceException;
	public void update (String username, Map<String,String> changes) throws ServiceException;
	public void delete (String username) throws ServiceException;
	public void delete (User user) throws ServiceException;
	public User get (String username) throws ServiceException;
	public User get (BigInteger id) throws ServiceException;
}
