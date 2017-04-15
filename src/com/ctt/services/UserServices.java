package com.ctt.services;

import com.ctt.daos.UserDao;
import com.ctt.dataclasses.UserEntity;
import com.ctt.utils.CacheUtil;

public class UserServices extends UserDao{

	public String createUser(UserEntity user) throws Exception
	{
		return super.createUser(user);
	}
	public UserEntity getUserInfo(String token)
	{
		return super.getUserInfo(getUserByToken(token).getUserName());
	}
	public UserEntity updateUser(UserEntity user)
	{
		return super.updateUser(user);
	}
	public UserEntity getUserByToken(String token)
	{
		UserEntity user=(UserEntity) new CacheUtil().getcache(token);
		return user;
	}
}
