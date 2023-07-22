package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.controller.AuthenticationService;
import com.example.demo.controller.RegisterRequest;
import com.example.demo.entity.Business;
import com.example.demo.entity.Role;

import static com.example.demo.entity.Role.ADMIN;
import static com.example.demo.entity.Role.MANAGER;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.username("admin")
					.password("password")
					.businessName("businessname")
					.ownerName("ownername")
					.businessContactNumber(123456789)
					.address("address")
					.email("admin@mail.com")
					.category("bakery")
					.aadharNumber("112345678902")
					.bankAccoutNo(123456789)
					.description("description")
					.panCardNo("12345678903")
					.fssaiNumber("12345678904")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.username("manager")
					.password("password")
					.businessName("businessname")
					.ownerName("ownername")
					.businessContactNumber(1234567890)
					.address("address")
					.email("manager@mail.com")
					.category("bakery")
					.aadharNumber("112345678905")
					.bankAccoutNo(1234567890)
					.description("description")
					.panCardNo("12345678906")
					.fssaiNumber("12345678907")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}

}
