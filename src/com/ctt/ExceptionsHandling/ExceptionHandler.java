package com.ctt.ExceptionsHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.ctt.utils.Utils;

public class ExceptionHandler implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		// TODO Auto-generated method stub
		System.out.println("Exception: "+exception.getMessage());
		return Utils.response(Response.Status.EXPECTATION_FAILED, "{\"Exception\":"+exception.getMessage()+"}");
	}

}
