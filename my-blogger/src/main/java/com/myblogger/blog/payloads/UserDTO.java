package com.myblogger.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.myblogger.blog.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

	private int id;
	
	
	@NotEmpty@Size(min=4,message="UserName  must be minimum of 4 characters")
	private String Name;
	
	@Email(message="Invalid Email adress")
	private String email;
	
	@NotEmpty
	@Size(min=3,max=10,message="Password must be in range of 3-10 characters")
	private String password;
	
	@NotEmpty
	private String about;
	
	
	private Set<RoleDto> roles=new HashSet<>();
	
}
