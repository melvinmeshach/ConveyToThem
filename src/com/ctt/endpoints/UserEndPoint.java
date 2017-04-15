package com.ctt.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.ctt.dataclasses.UserEntity;
import com.ctt.enums.UserAccessLevel;
import com.ctt.services.UserServices;
import com.ctt.services.filters.annotation.Authentication;
import com.ctt.utils.Utils;
@Path("/user")
public class UserEndPoint {
	@POST
	public Response createUser(UserEntity user)throws Exception
	{
		return Utils.response(Response.Status.OK,new UserServices().createUser(user));
	}
	@GET
	@Authentication(userAccessLevel=UserAccessLevel.READ_ONLY)
	public Response getUserInfo(@Context HttpServletRequest request)throws Exception
	{
		String token=request.getHeader("Authorization");
		return Utils.response(Response.Status.OK,Utils.objectToJson(new UserServices().getUserInfo(token)));
	}
	@PUT
	@Authentication(userAccessLevel=UserAccessLevel.READ_WRITE)
	public Response updateUser(UserEntity user)throws Exception
	{
		System.out.println("put method");
		//Exception occurs here
		System.out.println(user.getFirstName()+user.getLastName());
		return Utils.response(Response.Status.OK,Utils.objectToJson(new UserServices().updateUser(user)));
	}
}
