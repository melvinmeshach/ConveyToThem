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
		doHeaderCheck(requestContext);
	}
	private void doHeaderCheck(ContainerRequestContext requestContext) throws IOException{
		if (!requestContext.getHeaders().containsKey(AUTH_HEADER)) {
        	requestContext.abortWith(ACCESS_DENIED);
			return;
		} else if (!new CacheUtil().isKeyExists(requestContext.getHeaderString("Authorization"))) {
        	requestContext.abortWith(ACCESS_DENIED);
			return;
		} 
		if (userRoll != UserAccessLevel.NO_LIMITATION) {
			doReadAccessCheck(requestContext);
		}
	
	}

	private void doReadAccessCheck(ContainerRequestContext requestContext) throws IOException {
		if (userRoll == UserAccessLevel.READ_ONLY) {
			//do the location check here
			return;
		}
		else if (userRoll == UserAccessLevel.READ_WRITE) {
			doReadWriteAccessCheck(requestContext);
		}
	}

	private void doReadWriteAccessCheck(ContainerRequestContext requestContext) throws IOException {
		System.out.println("inside doReadWriteAccessCheck method");
		CacheUtil cacheUtil = new CacheUtil();
		UserEntity user=(UserEntity)cacheUtil.getcache(requestContext.getHeaders().get(AUTH_HEADER).get(0));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(requestContext.getEntityStream(), baos);
        byte[] jsonBytes = baos.toByteArray();
        String json = new String(jsonBytes, "UTF-8");
        requestContext.setEntityStream(new ByteArrayInputStream(jsonBytes));
        UserEntity updatingUser=(UserEntity)Utils.jsonToObject(json,UserEntity.class);
        if(!user.getUserName().equals(updatingUser.getUserName()))
      		{
              	System.out.println("Aborting in doReadWirteAccessCheck because Read_write ACCESS_DENIED");
              	requestContext.abortWith(ACCESS_DENIED);
      		}
    }
}
