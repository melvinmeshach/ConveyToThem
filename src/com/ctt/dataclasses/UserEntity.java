package com.ctt.dataclasses;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Getter;
import lombok.Setter;
@Entity

@Index
public class UserEntity implements Serializable{
	@Getter@Setter
	private String firstName;
	@Getter@Setter
	private String lastName;
	@Id
	@Getter@Setter
	private String userName;
	@Setter
	private String password;
	@Getter@Setter
	private LocationEntity location;
	@JsonIgnore
	public String getPasswordExclusively(){
		return password;
	}
}
