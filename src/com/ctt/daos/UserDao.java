package com.ctt.daos;

import com.ctt.dataclasses.UserEntity;
import com.ctt.ofyservices.OfyService;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;

public class UserDao extends OfyService{

	public String createUser(UserEntity user) throws Exception
	{
		super.save(user);
		String key=Utils.createUUID();
		CacheUtil cache=new CacheUtil();
		cache.setCache(key, user);
		return "{\"creationStatus\": true,\"userKey\":"+key+"}";
	}
	public UserEntity getUserInfo(String userName)
	{
		return super.get(UserEntity.class,userName);
	}
	public UserEntity updateUser(UserEntity user)
	{
		return super.save(user);
	}
}
