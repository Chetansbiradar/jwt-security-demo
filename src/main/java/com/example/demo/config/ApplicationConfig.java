package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	@Autowired
	private BusinessRepository repository;
	@Bean
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				return repository.findById(Integer.parseInt(id)).orElseThrow(()-> new UsernameNotFoundException("User Not found"));
			}
		};
	}
	
	
	//AuthenticationProvider is a data access object which is responsible to fetch user details and encode user password etc
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		//we need to specify two properties .. 1-userDetailsService  and 2.. specify password encoder
		 
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	//authentication manager - responsible to manage authentication
	@Bean
	public AuthenticationManager authenticationManger(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}
}
