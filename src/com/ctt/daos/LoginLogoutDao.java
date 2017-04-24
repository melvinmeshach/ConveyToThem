package com.ctt.daos;

import javax.ws.rs.core.Response;

import com.ctt.ExceptionsHandling.GlobalAppException;
import com.ctt.dataclasses.UserEntity;
import com.ctt.ofyservices.OfyService;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;
import javax.ws.rs.core.Response.Status;
public class LoginLogoutDao extends OfyService{
	public UserEntity userLogin(String userName,String password) throws Exception
	{
		UserEntity user=super.get(UserEntity.class, userName);
		String key;
		if(user.getPasswordExclusively().equals(password))
		{
			return user;
			}
		else{
			throw new GlobalAppException("Login Failed",Status.UNAUTHORIZED);
		}
	}
}
