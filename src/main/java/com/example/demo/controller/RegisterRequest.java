package com.example.demo.controller;

import com.example.demo.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

	private int id;
	private String username;
	private String password;
	private String businessName;
	private String ownerName;
	private int businessContactNumber;
	private String address;
	private String email;
	private String category;
	private String aadharNumber;
	private int bankAccoutNo;
	private String panCardNo;
	private String fssaiNumber;
	private String description;
	private Role role;
}
