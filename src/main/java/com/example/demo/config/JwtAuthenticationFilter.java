package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.token.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor	//it will create constructor for any final field created in this class 
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//=================================
		if (request.getServletPath().contains("/api/business/auth")) {
		      filterChain.doFilter(request, response);
		      return;
		    }
		//==========================================
		//Jwt auth token is available within header // authorization contains the bearer token
		final String authHeader =request.getHeader("Authorization");
		final String jwtToken;
		final String userName;
		
		// check JwtToken is null or it doesnt start with Bearer
		if(authHeader == null || !authHeader.startsWith("Bearer "))
		{
			//passes info to next filter and after v wont continue the process hence return statement
			filterChain.doFilter(request, response);
			return ;
		}
		
		//we have the token after the Bearer(6)characters
		jwtToken=authHeader.substring(7);
		
		//to extract the username we need a class which can extract it from jwtToken
		userName=jwtService.extractUsername(jwtToken);
		
		//if userName is  not null and he has not been authenticated earlier then do this
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{ 
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			
			
			//==========================
			var isTokenValid = tokenRepository.findByToken(jwtToken)
			          .map(t -> !t.isExpired() && !t.isRevoked())
			          .orElse(false);
			//=============================
			if(jwtService.isTokenValid(jwtToken, userDetails))
			{
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
	

}




