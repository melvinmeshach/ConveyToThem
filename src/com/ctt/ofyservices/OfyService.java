package com.ctt.ofyservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ctt.dataclasses.UserEntity;
import com.fullauth.api.utils.Utils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class OfyService
{
    static
    {
    	System.out.println("Static block is called and registered");
        factory().register( UserEntity.class );
      //  factory().register(Location.class);
      }

    private static ObjectifyFactory factory()
    {
        return ObjectifyService.factory();
    }

    private static Objectify ofy()
    {
        return ObjectifyService.ofy();
    }

    public <T> T get( Class<T> tClass , String id )
    {
        return ofy().load().type( tClass ).id( id ).now();
    }

    public <T> List<T> get( Class<T> tClass , List<String> ids )
    {
        Map<String, T> entityMap =   ofy().load().type(tClass).ids(ids);
        Collection<T> values     =   entityMap.values();
        List<T> entityList       =   new ArrayList<T>(values);
        return entityList;
    }

    public <T> T save( T entity )
    {
    	 Key<T> key = ofy().save().entity( entity ).now();

         if( key== null )
             return null;

         return entity;
    }

    public <T> boolean delete(T entity)
    {
        if (entity == null)
            return false;

        ofy().delete().entity(entity).now();
        return true;
    }

    public <T> T getByFilter(Class<T> clazz, String filterBy, Object value)
    {
        if ( value == null || Utils.isNullOrEmpty(filterBy) )
            throw new IllegalArgumentException("Invalid Argument value/filterBy");

        return ofy().load().type(clazz).filter(filterBy, value).first().now();
    }

    public <T> List<T> getEntitiesByFilter(Class<T> clazz, String filterBy, Object value)
    {
        if (value == null || Utils.isNullOrEmpty(filterBy))
            throw new IllegalArgumentException("Invalid Argument value / filterBy");
        return ofy().load().type(clazz).filter(filterBy, value).list();
    }

    public <T> Object getByFilters(Class<T> clazz, List<OfyFilter> filters)
    {
        if (Utils.isNullOrEmpty(filters))
            throw new IllegalArgumentException("Invalid Argument filters");

        Query q = ofy().load().type(clazz);

        for(OfyFilter filter: filters)
        {
            q.filter(filter.parameter + " " + filter.operation, filter.value);
        }

        return q.first().now();
    }

    public <T> List<T> getEntitiesByFilters(Class<T> clazz, List<OfyFilter> filters)
    {
        if (Utils.isNullOrEmpty(filters))
            throw new IllegalArgumentException("Invalid Argument filters");

        Query q = ofy().load().type(clazz);

        for(OfyFilter filter: filters)
        {
            q.filter(filter.parameter + " " + filter.operation, filter.value);
        }

        return q.list();
    }
}
