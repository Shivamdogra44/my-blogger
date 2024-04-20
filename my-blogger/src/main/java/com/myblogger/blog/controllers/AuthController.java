package com.myblogger.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblogger.blog.exceptions.ApiExceptions;
import com.myblogger.blog.payloads.JwtAuthRequest;
import com.myblogger.blog.payloads.JwtAuthResponse;
import com.myblogger.blog.payloads.UserDTO;
import com.myblogger.blog.security.JwtTokenHelper;
import com.myblogger.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired  
	private JwtTokenHelper jwtTokenHelper;
	 @Autowired  
	private UserDetailsService userDetailsService;
	 @Autowired
	 private AuthenticationManager authenticationManager;
	 @Autowired
	 private UserService  userService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request)
	{
		System.out.println(request.getUsername());
		this.authenticate(request.getUsername(),request.getPassword());
		
		UserDetails details=this.userDetailsService.loadUserByUsername(request.getUsername());
		String token=this.jwtTokenHelper.generateToken(details);
		
		JwtAuthResponse response = JwtAuthResponse.builder()
                .jwtToken(token)
                .username(details.getUsername()).build();
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
		
	}

	private void authenticate(String email, String password) {
		
		  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	        try {
	            authenticationManager.authenticate(authentication);


	        } catch (BadCredentialsException e) {
	            throw new ApiExceptions(" Invalid Username or Password  !!");
	        }

	    }

	    @ExceptionHandler(BadCredentialsException.class)
	    public String exceptionHandler() {
	        return "Credentials Invalid !!";
	    }
	    @PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userdto){
		UserDTO registeredUser=this.userService.registerNewUser(userdto);
		return new ResponseEntity<UserDTO>(registeredUser,HttpStatus.CREATED);
	}
}
