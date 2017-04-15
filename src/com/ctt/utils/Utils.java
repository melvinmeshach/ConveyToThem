package com.ctt.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.ctt.dataclasses.UserEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Utils {
	 private static ObjectMapper mapper = new ObjectMapper();

	    public static String createUUID()
	    {
	        return UUID.randomUUID().toString();
	    }

	    public static Response response(Response.Status status, String message)
	    {
	        return Response.status( status ).entity( message ).build();
	    }



	    public static Map<String, Object> jsonToMap( String jsonStr )
	    {
	        try
	        {
	            return mapper.readValue( jsonStr , new TypeReference<HashMap<String, Object>>() {});
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    public static Object jsonToObject(String jsonString,Class objectClass) throws JsonParseException, JsonMappingException, IOException
	    {
	    	return mapper.readValue(jsonString, objectClass);
	    }

	    public static String objectToJson( Object jsonStr ) throws JsonProcessingException
	    {
	    	/*if(jsonStr instanceof User){
	    		System.out.println("inside instance check");
	    		ObjectWriter writer=mapper.writer().withoutAttribute("password");
	    		return writer.writeValueAsString(jsonStr);
	    	}*/
	        try
	        {
	            return mapper.writeValueAsString(jsonStr);
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
