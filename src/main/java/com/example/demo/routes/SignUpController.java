package com.example.demo.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
public class SignUpController {

	@GetMapping()
	public ResponseEntity<String> sayHello()
	{
		return ResponseEntity.ok("Hello from secured nedPoint ");
	}
}
