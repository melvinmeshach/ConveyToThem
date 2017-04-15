package com.ctt.services.filters;

/**
 * @author ronak
 * Static file to define priorities for different filters
 * @info The higher the value the lower the order. Authentication will occur after CacheControl
 * **/

public class Priorities 
{
	private Priorities() {
        // prevents construction
    }
    public static final int AUTHENTICATION = 1;
}
