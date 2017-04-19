package com.ctt.services;

import java.io.IOException;
import java.util.Map;

import com.ctt.daos.UserDao;
import com.ctt.dataclasses.LocationEntity;
import com.ctt.dataclasses.UserEntity;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserServices extends UserDao{

	public String createUser(UserEntity user) throws Exception
	{
		return super.createUser(user);
	}
	public UserEntity getUserInfo(String token)
	{
		return super.getUserInfo(getUserByToken(token).getUserName());
	}
	public UserEntity updateUser(String clientToken,Map<String,Object> updateData) throws JsonParseException, JsonMappingException, IOException
	{
		
		UserEntity user=getUserByToken(clientToken);
/*		for(String attribute:updateData.keySet()){
			if(attribute.equals("firstName")){
				user.setFirstName(updateData.get(attribute).toString());
			}
			else if(attribute.equals("lastName")){
				user.setLastName(updateData.get(attribute).toString());
			}
			else if(attribute.equals("password")){
				user.setPassword(updateData.get(attribute).toString());
			}
			else if(attribute.equals("location")){
				user.setLocation((LocationEntity)updateData.get(attribute));
			}
		}*/
		if(updateData.containsKey("firstName"))
			user.setFirstName(updateData.get("firstName").toString());
		if(updateData.containsKey("lastName"))
			user.setLastName(updateData.get("lastName").toString());
		if(updateData.containsKey("password"))
			user.setPassword(updateData.get("password").toString());
		if(updateData.containsKey("location"))
			user.setLocation((LocationEntity)Utils.jsonToObject(Utils.objectToJson(updateData.get("location")),LocationEntity.class));
		return super.updateUser(user);
	}
	public UserEntity getUserByToken(String token)
	{
		UserEntity user=(UserEntity) new CacheUtil().getcache(token);
		return user;
	}
}
