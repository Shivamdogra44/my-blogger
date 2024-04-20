package com.myblogger.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.myblogger.blog.security.CustomerUserDetailsService;
import com.myblogger.blog.security.JwtAuthenticationEntryPoint;
import com.myblogger.blog.security.JwtAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity

public class SecurityConfig{

	public static final String[] PUBLIC_URLS= {"/api/v1/auth/**","/v3/api-docs",
			"/api/v2/api-docs","/sawgger-resources/**","/sawgger-ui/**",
			"/webjars/**",
			
			
	};
	
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception
	{
	http
	.csrf(csrf->csrf.disable())
	.authorizeHttpRequests(authorize->authorize.requestMatchers(PUBLIC_URLS)
	.permitAll()
	.requestMatchers(HttpMethod.GET).permitAll()
	.anyRequest()
	.authenticated())
	.exceptionHandling(ex->ex.authenticationEntryPoint(this.jwtAuthenticationEntryPoint))
	.sessionManagement(session->session .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	;
	
	http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	
	http.authenticationProvider(daoAuthenticationProvider());
	DefaultSecurityFilterChain chain=http.build();

	
	return chain;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		}
	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
	
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customerUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
			
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception
	{
		return configuration.getAuthenticationManager();
	}
	
	
}
