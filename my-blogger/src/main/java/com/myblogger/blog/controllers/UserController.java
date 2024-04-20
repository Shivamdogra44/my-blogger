package com.myblogger.blog.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblogger.blog.payloads.ApiResponse;
import com.myblogger.blog.payloads.UserDTO;
import com.myblogger.blog.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name="User controller",description="performing CRUD operations on User")
//@SecurityRequirement(name="authBearer")
public class UserController {
	

	@Autowired
	private UserService  userServices;
	
	@Operation(summary = "Post operation on user",description="creating user in data base")
	@PostMapping("/")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO)
	{
		UserDTO createUserDTO=this.userServices.createUser(userDTO);
		return new ResponseEntity<>(createUserDTO,HttpStatus.CREATED);
	}
	@Operation(summary = "Update operation on user",description="Update user in data base by id")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO>updateUser(@RequestBody UserDTO UserDto,@PathVariable("userId") Integer uid)
	{
		UserDTO updatedUser=this.userServices.updateUser(UserDto, uid);
		return ResponseEntity.ok(updatedUser);
	}
	@Operation(summary = "Delete operation on user",description="Delete user from data base by id")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId")Integer uid){
		
		this.userServices.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully",true),HttpStatus.OK);
	}
	@Operation(summary = "Get operation on user",description="retrieving user from data base")
	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUser(){
		return ResponseEntity.ok(this.userServices.getAllUsers());
	}
	@Operation(summary = "Get operation on user",description="retrieving user from data base by id")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId){
		return ResponseEntity.ok(this.userServices.getUserById(userId));
	}
}

