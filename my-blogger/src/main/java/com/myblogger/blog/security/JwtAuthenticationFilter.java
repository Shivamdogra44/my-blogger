package com.myblogger.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


		
		//1 get Token
		String requestToken=request.getHeader("Authorization");
		
		System.out.println(requestToken);
		
		String userName=null;
		String token=null;
		
		
		if(requestToken!=null && requestToken.startsWith("Bearer"))
		{
			
			token = requestToken.substring(7).trim();
			try {
			userName=this.jwtTokenHelper.getUsernameFromToken(token);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("Unable to get JWT token");
			}
			catch(ExpiredJwtException e)
			{
				System.out.println("Jwt Token expired");
			}
			catch(MalformedJwtException e)
			{
				System.out.println("invalid jwt");
			}
		}
		else
		{
			System.out.println("Jwt token does not begin with Bearer");
		}
		
		//once Token received, validate the token
		
		
		if(userName!=null&& SecurityContextHolder.getContext().getAuthentication()==null)
		{
		UserDetails userDetails=	this.userDetailsService.loadUserByUsername(userName);
		Boolean validateToken = this.jwtTokenHelper.validateToken(token, userDetails);
			if(validateToken)
			{
				//working fine
				//authentication required to set
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else
			{
				System.out.println("Invalid jwt token");
			}
		}
		else
		{
			System.out.println("UserName is null or Context is not null");
		}
		
		
		filterChain.doFilter(request, response);
		
	}
	

}/*
package com.myblogger.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Token
        String requestToken = request.getHeader("Authorization");

        System.out.println(requestToken);

        String userName = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer")) {
            // Fix: Assign the result to the 'token' variable
            token = requestToken.substring(7).trim(); // Extracting the token excluding "Bearer "
            try {
                userName = this.jwtTokenHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                System.out.println("Jwt Token expired");
            } catch (MalformedJwtException e) {
                System.out.println("Invalid jwt");
            }
        } else {
            System.out.println("Jwt token does not begin with Bearer");
        }

        // 2. Once Token received, validate the token
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            Boolean validateToken = this.jwtTokenHelper.validateToken(token, userDetails);
            if (validateToken) {
                // Working fine
                // Authentication required to set
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Invalid jwt token");
            }
        } else {
            System.out.println("UserName is null or Context is not null");
        }

        filterChain.doFilter(request, response);
    }
}*/