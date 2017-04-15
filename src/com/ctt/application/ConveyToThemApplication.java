package com.ctt.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import com.ctt.ExceptionsHandling.ExceptionHandler;
import com.ctt.endpoints.UserEndPoint;
import com.ctt.filters.OPTIONSMockFilter;
import com.ctt.services.filters.RegisterAuthenticationFilter;

/**
 * Created by anandthiyagarasu on 31/01/17.
 */
@SuppressWarnings("unchecked")
public class ConveyToThemApplication extends Application
{
	private Set singletons  = new HashSet();
    private Set  classes    = new HashSet();
    private CorsFilter  corsFilter = new CorsFilter();
    
    
	public ConveyToThemApplication() {
    	
	    corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);

        //Endpoints
        singletons.add(new UserEndPoint());
        singletons.add(new ExceptionHandler());
        //Filters
        classes.add(OPTIONSMockFilter.class);
        classes.add(RegisterAuthenticationFilter.class);
	}

    @Override
    public Set<Class<?>> getClasses()
    {
       return classes;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
