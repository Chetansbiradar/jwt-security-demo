package com.example.demo.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, String> {
	
	Optional<Business> findByUsername(String username);
}
