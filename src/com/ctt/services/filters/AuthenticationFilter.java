package com.ctt.services.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.ctt.dataclasses.UserEntity;
import com.ctt.enums.UserAccessLevel;
import com.ctt.utils.CacheUtil;
import com.ctt.utils.Utils;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.core.ServerResponse;

@SuppressWarnings("unchecked")
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	private UserAccessLevel userRoll = UserAccessLevel.NO_LIMITATION;
	private CacheUtil cacheUtil = new CacheUtil();

	private final static String AUTH_HEADER = "Authorization";

	private static final ServerResponse ACCESS_DENIED = new ServerResponse("Authorization Failed", 401, null);

	public AuthenticationFilter(UserAccessLevel roll) {
		this.userRoll = roll;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("Inside filter method");
		checkAuthenticationAndPermission(requestContext);
	}

	private void checkAuthenticationAndPermission(ContainerRequestContext requestContext) throws IOException {
		System.out.println("Inside checkAuthenticationAndPermission method");

		if (userRoll != UserAccessLevel.NO_LIMITATION) {
			doLimitationsCheck(requestContext);
		}

	}

	private void doLimitationsCheck(ContainerRequestContext requestContext) throws IOException {
		System.out.println("Inside doLimitationCheck method with the user access level: "+userRoll);
		if (!requestContext.getHeaders().containsKey(AUTH_HEADER)) {
        	System.out.println("Aborting in doLimitationsCheck because(No Authorization Header Present) ACCESS_DENIED");
        	requestContext.abortWith(ACCESS_DENIED);
			return;
		} else if (!new CacheUtil().isKeyExists(requestContext.getHeaderString("Authorization"))) {
        	System.out.println("Aborting in doLimitationsCheck(Authorization Key is not present in cache) because ACCESS_DENIED");
        	requestContext.abortWith(ACCESS_DENIED);
			return;
		} else if (userRoll == UserAccessLevel.READ_WRITE) {
			doReadWriteAccessCheck(requestContext);
		}
	}

	private void doReadWriteAccessCheck(ContainerRequestContext requestContext) throws IOException {
		System.out.println("inside doReadWriteAccessCheck method");
		CacheUtil cacheUtil = new CacheUtil();
		UserEntity user=(UserEntity)cacheUtil.getcache(requestContext.getHeaders().get(AUTH_HEADER).get(0));
		//System.out.println("doReadWriteAccessCheck: "+user.getFirstName()+user.getLastName());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(requestContext.getEntityStream(), baos);
        byte[] jsonBytes = baos.toByteArray();
        String json = new String(jsonBytes, "UTF-8");
        //again setting the entities in the requestContext
        requestContext.setEntityStream(new ByteArrayInputStream(jsonBytes));
        //System.out.println("json string:"+json);
        UserEntity updatingUser=(UserEntity)Utils.jsonToObject(json,UserEntity.class);
       /* Object object = Utils.jsonToObject(json,Object.class);
        System.out.println("After json to object");
        if(object instanceof UserEntity){
        	System.out.println("it is an instance of userentity");
        	UserEntity updatingUser=(UserEntity)object;
        if(!user.getUserName().equals(updatingUser.getUserName()))
		{
        	System.out.println("Aborting in doReadWirteAccessCheck because Read_write ACCESS_DENIED");
        	requestContext.abortWith(ACCESS_DENIED);
		}
        System.out.println("End of doReadWriteAccessCheck");
        }*/
        //System.out.println("name: "+updatingUser.getFirstName()+updatingUser.getLastName());
        //System.out.println("UserName1:"+user.getUserName()+"UserName2:"+updatingUser.getUserName());
        if(!user.getUserName().equals(updatingUser.getUserName()))
      		{
              	System.out.println("Aborting in doReadWirteAccessCheck because Read_write ACCESS_DENIED");
              	requestContext.abortWith(ACCESS_DENIED);
      		}
    //    System.out.println("End of doReadWriteAccessCheck");
	}
}
