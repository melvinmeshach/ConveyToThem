package com.ctt.services.filters.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ctt.enums.UserAccessLevel;

@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication
{
	UserAccessLevel userAccessLevel() default UserAccessLevel.NO_LIMITATION;
}