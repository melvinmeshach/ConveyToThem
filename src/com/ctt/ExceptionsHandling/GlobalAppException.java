package com.ctt.ExceptionsHandling;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;


public class GlobalAppException extends Exception{
	private static final long serialVersionUID = 1L;
	protected Status status;
	public GlobalAppException(String message, Status status)
	{
		super(message);
		this.status = status;
	}
	@Override
	public String toString()
    {
    	return "{\"response\":false, \"Exception\":\""+StringEscapeUtils.escapeJava(getMessage())+"\"}";
    }
}
