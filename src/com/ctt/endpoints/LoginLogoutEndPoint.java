package com.ctt.endpoints;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.ctt.enums.UserAccessLevel;
import com.ctt.services.LoginLogoutServices;
import com.ctt.services.UserServices;
import com.ctt.services.filters.annotation.Authentication;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;

@Path("/")
public class LoginLogoutEndPoint {
	@Path("/login")
	@POST
	public Response login(Map<String,String> loginData) throws Exception{
		return Utils.response(Response.Status.OK,new LoginLogoutServices().userLogin(loginData));
	} 
	@Path("logout")
	@POST
	@Authentication(userAccessLevel=UserAccessLevel.NO_LIMITATION)
	public Response logout(@Context HttpServletRequest request )throws Exception{
		return Utils.response(Response.Status.OK, new LoginLogoutServices().userLogout(request));
	}
}
