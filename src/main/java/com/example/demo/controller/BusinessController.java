package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/business/auth")
@RequiredArgsConstructor
public class BusinessController {

	private AuthenticationService service;
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
	{
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
	{
		return ResponseEntity.ok(service.authenticate(request));
	}
	
	@PostMapping("/refresh-token")
	  public void refreshToken(
	      HttpServletRequest request,
	      HttpServletResponse response
	  ) throws java.io.IOException {
	    service.refreshToken(request, response);
	  }
	
}
