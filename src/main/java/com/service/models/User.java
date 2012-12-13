package com.service.models;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
	
	@Id
	private BigInteger id;
	private String username;
	private Profile profile;
	private String password;
	private Map<String, Map<String,String>> services;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
 
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Map<String, Map<String, String>> getServices() {
		return services;
	}
	public void setServices(Map<String, Map<String, String>> services) {
		this.services = services;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}
 
	
	
}
