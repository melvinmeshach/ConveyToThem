package com.ctt.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

public class OPTIONSMockFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		 if (requestContext.getRequest().getMethod().equalsIgnoreCase("OPTIONS"))
	        {
	            requestContext.abortWith(   Response.status(200).build()   );
	            return;
	        }
		 }

}
