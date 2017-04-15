package com.ctt.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.base.Strings;

public class CacheUtil
{
	private static final Logger log = Logger.getLogger(CacheUtil.class.getName());
	
	public String setCache(String key, Object data) throws Exception
	{
		ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
		objectOutputStream.writeObject(data);			
		objectOutputStream.flush();
		objectOutputStream.close();
		byteObject.close();

		byte[] fileBytes = byteObject.toByteArray();
		int offset 				= 0;
		HashMap<String,Object> transcriptMap	= new HashMap<String,Object>();
		int count				= 0;
		int nextkeycounter		= 0;	
		int iterationreq		=	fileBytes.length / 900000+1;
		while (offset < fileBytes.length)
		{
			try
			{
				transcriptMap	= new HashMap<String,Object>();
				byte[] outputBytes;
	
				if(fileBytes.length - offset < 900000 )
				{
					outputBytes = new byte[fileBytes.length - offset];
					System.arraycopy(fileBytes, offset, outputBytes, 0, fileBytes.length - offset);
					transcriptMap.put("value", outputBytes);
					if(count+1<iterationreq)
					{
						nextkeycounter = count+1;
						transcriptMap.put("nextkey", key+"_"+nextkeycounter);
					}
					else
					{
						transcriptMap.put("nextkey","");
					}
					
					if(count != 0)
						persistDataToCache(key+"_"+count,transcriptMap);
					else
						persistDataToCache(key,transcriptMap);
					count	=	count+1;
					break;
				}
			
				outputBytes = new byte[900000];
				System.arraycopy(fileBytes, offset, outputBytes, 0, 900000);
				offset +=900000 ; 
				transcriptMap.put("value", outputBytes);
				
				if(count+1<iterationreq)
				{
					nextkeycounter = count+1;
					transcriptMap.put("nextkey",key+"_"+nextkeycounter);
				}else
				{
					transcriptMap.put("nextkey","");
				}
				if(count != 0)
				persistDataToCache(key+"_"+count,transcriptMap);
				else
				persistDataToCache(key,transcriptMap);
				count	=	count+1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}
		return "success";
	}
	private void persistDataToCache(String key,HashMap<String,Object> map)
	{
		Object datao = map;
		log.info("key "+key);
		try 
		{
			if(map!=null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(out);
				os.writeObject(datao);
				byte[] value= out.toByteArray();
				MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
				syncCache.put(key, value); 
			}
		} 
		catch (Exception e) 
		{
			log.log(Level.SEVERE,"Error in persistDataToCache() "+e.getMessage(),e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<Object,Object> getDataFromCache(String key)
	{
		HashMap<Object,Object> result 					=	null;
		ObjectInputStream lobjectInputStream 			= 	null;
		Object valueFromCache 							= 	null;

		try 
		{
			MemcacheService syncCache 					= 	MemcacheServiceFactory.getMemcacheService();
			
			syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			
			byte[] value	 							=	(byte[]) syncCache.get(key); 
			
			if (value != null) 
			{
				ByteArrayInputStream lbyteInputStream 	= 	new ByteArrayInputStream(value);
				lobjectInputStream 						=	new ObjectInputStream(lbyteInputStream);
				valueFromCache 							=	lobjectInputStream.readObject();
				lbyteInputStream.close();
				lobjectInputStream.close();
				result 									=	(HashMap<Object, Object>) valueFromCache;
			}
		} 
		catch (Exception e) 
		{
			log.log(Level.SEVERE,"Error getting data from local cache"+e.getMessage(),e);
		}
		return result;
	}
	public String deleteCache(String key)
	{
		boolean isDeleted 						= 		false;
		String deleted							=		"success";	
		String deletedkey						=		"";	
		String keytoDelete						=		"";
		int keycounter							=		0;
		try 
		{
			while(deleted == "success")
			{
				if(keycounter == 0)
				{
					keytoDelete  = key;
				}else{
					keytoDelete	=	key+"_"+keycounter;
				}
				MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
				isDeleted 	=	syncCache.delete(keytoDelete);
				deletedkey	=	deletedkey+" ::"+isDeleted+",";
				if(isDeleted == true)
					deleted = "success";
				else
					deleted = "failed";
			
				keycounter = keycounter+1;
			}
			return deletedkey;
		} 
		catch (Exception e) 
		{
			log.log(Level.SEVERE,"Error getting data from local cache"+e.getMessage(),e);
			return "failed";
		}
		
	}
	public Object getcache(String key)
	{	
		byte[] objectContent 				  = null;
		HashMap <Object , Object> memCacheMap = null;
		Object valueFromCache 				  = null;
		boolean isSkip = false;
		try
		{
			ObjectInputStream lobjectInputStream 	= 		null;
			memCacheMap = getDataFromCache(key);
			if (memCacheMap != null)
			{
				objectContent = new byte[0];
				isSkip = true;
				while (!Strings.isNullOrEmpty(key))
				{
					if (isSkip)
						isSkip = false;
					else
						memCacheMap = getDataFromCache(key);
	
					byte[] tempList = (byte[]) memCacheMap.get("value");
					byte[] tempOriginal = objectContent;
					objectContent = new byte[objectContent.length+ tempList.length];
	
					System.arraycopy(tempOriginal, 0, objectContent, 0,tempOriginal.length);
					System.arraycopy(tempList, 0, objectContent,tempOriginal.length, tempList.length);
	
					key = (String) memCacheMap.get("nextkey");
				}
			}
			if (objectContent != null) 
			{
				ByteArrayInputStream lbyteInputStream 	= 	new ByteArrayInputStream(objectContent);
				lobjectInputStream 						=	new ObjectInputStream(lbyteInputStream);
				valueFromCache 							=	lobjectInputStream.readObject();
				lbyteInputStream.close();
				lobjectInputStream.close();
			}
		}
		catch ( Exception e )
		{
			log.log(Level.SEVERE,"Error in getting cache so returning null getDatafromCachenew()"+e.getMessage(),e);
			return null;
		}
		return valueFromCache;
		
	}

	public boolean isKeyExists(String key)
	{
		boolean result							=	false;
		try
		{
			MemcacheService syncCache 				=	MemcacheServiceFactory.getMemcacheService();
			syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			if (syncCache.contains(key)) 
				result = true;
			log.info("KEY got is ::" + key +" "+result);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE,"Error in getting contains key"+ e.getMessage(), e);
		}
		return result;
	}
}