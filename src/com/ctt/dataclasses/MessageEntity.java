package com.ctt.dataclasses;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
public class MessageEntity implements Serializable{
	@Id
	@Getter@Setter
	private String userName;
	@Getter@Setter
	private String title;
	@Getter@Setter
	private String message;
	@Getter@Setter
	private LocationEntity location;
}
