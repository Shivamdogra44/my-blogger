package com.myblogger.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myblogger.blog.entities.User;
import com.myblogger.blog.exceptions.ResourceNotFoundException;
import com.myblogger.blog.repositories.UserRepo;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// loading user from database
		User user=this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", "Email :"+username, 0));
		return user;
	}

}
