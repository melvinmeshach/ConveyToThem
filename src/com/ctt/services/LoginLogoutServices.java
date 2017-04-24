package com.ctt.services;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ctt.daos.LoginLogoutDao;
import com.ctt.dataclasses.UserEntity;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;

public class LoginLogoutServices extends LoginLogoutDao {
	public String userLogin(Map<String,String> loginData) throws Exception
	{
		String userName=loginData.get("userName");
		String password=loginData.get("password");
		UserEntity user=userLogin(userName, password);
		String key=addUserEntityToCache(user);
		return "{\"user\":"+Utils.objectToJson(user)+",\"key\":"+key+"}"; 
	}
	public String userLogout(HttpServletRequest request) throws Exception
	{
		String userKey=request.getHeader("Authorization");
			return "{\"logoutSuccess\":"+deleteUserEntityFromCache(userKey)+"}";
	}
	public String addUserEntityToCache(UserEntity user) throws Exception{
		String key=Utils.createUUID();
		CacheUtil cache=new CacheUtil();
		cache.setCache(key, user);
		return key;
	}
	public boolean deleteUserEntityFromCache(String key){
		CacheUtil cache=new CacheUtil();
		cache.deleteCache(key);
			return true;
			}
}
