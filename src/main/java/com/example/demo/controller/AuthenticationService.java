package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtService;
import com.example.demo.entity.Business;
import com.example.demo.entity.BusinessRepository;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	@Autowired
	private BusinessRepository repository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		var user=Business.builder()
				.username(request.getUsername())
				.password(request.getPassword())
				.businessName(request.getBusinessName())
				.ownerName(request.getOwnerName())
				.businessContactNumber(request.getBusinessContactNumber())
				.address(request.getAddress())
				.email(request.getEmail())
				.category(request.getCategory())
				.aadharNumber(request.getAadharNumber())
				.bankAccoutNo(request.getBankAccoutNo())
				.description(request.getDescription())
				.panCardNo(request.getPanCardNo())
				.fssaiNumber(request.getFssaiNumber())
				.role(request.getRole())
				.build();
				
		var savedUser = repository.save(user);
	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefreshToken(user);
				
	    saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// TODO Auto-generated method stub
		authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				);
		var user =repository.findByUsername(request.getUsername()).orElseThrow();
		var jwtToken=jwtService.generateToken(user);
		
		
		var refreshToken = jwtService.generateRefreshToken(user);
	    revokeAllUserTokens(user);
	    saveUserToken(user, jwtToken);
	    
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}
	 private void saveUserToken(Business user, String jwtToken) {
		    var token = Token.builder()
		        .user(user)
		        .token(jwtToken)
		        .tokenType(TokenType.BEARER)
		        .expired(false)
		        .revoked(false)
		        .build();
		    tokenRepository.save(token);
		  }
	 
	 private void revokeAllUserTokens(Business user)
	 {
		    var validUserTokens = tokenRepository.findAllValidTokenByBusiness(user.getUsername());
		    if (validUserTokens.isEmpty())
		      return;
		    validUserTokens.forEach(token -> {
		      token.setExpired(true);
		      token.setRevoked(true);
		    });
		    tokenRepository.saveAll(validUserTokens);
	 }
	 
	 public void refreshToken(
	          HttpServletRequest request,
	          HttpServletResponse response
	  ) throws IOException {
	    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	    final String refreshToken;
	    final String userName;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    refreshToken = authHeader.substring(7);
	    userName = jwtService.extractUsername(refreshToken);
	    if (userName != null) {
	      var user = this.repository.findByUsername(userName)
	              .orElseThrow();
	      if (jwtService.isTokenValid(refreshToken, user)) {
	        var accessToken = jwtService.generateToken(user);
	        revokeAllUserTokens(user);
	        saveUserToken(user, accessToken);
	        var authResponse = AuthenticationResponse.builder()
	                .accessToken(accessToken)
	                .refreshToken(refreshToken)
	                .build();
	        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
	      }
	    }
	  }

}
