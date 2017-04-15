package com.ctt.services.filters;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.ctt.services.filters.AuthenticationFilter;
import com.ctt.services.filters.annotation.Authentication;

@Provider
public class RegisterAuthenticationFilter  implements DynamicFeature {
	public void configure(ResourceInfo ri, FeatureContext ctx) 
    {
		Authentication authenticationFilter = ri.getResourceMethod().getAnnotation(Authentication.class);
	      if( authenticationFilter == null ) return;
	      ctx.register(new AuthenticationFilter(authenticationFilter.userAccessLevel()));
    }
}

