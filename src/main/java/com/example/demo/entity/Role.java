package com.example.demo.entity;

import static com.example.demo.entity.Permission.ADMIN_CREATE;
import static com.example.demo.entity.Permission.ADMIN_DELETE;
import static com.example.demo.entity.Permission.ADMIN_READ;
import static com.example.demo.entity.Permission.ADMIN_UPDATE;
import static com.example.demo.entity.Permission.MANAGER_CREATE;
import static com.example.demo.entity.Permission.MANAGER_DELETE;
import static com.example.demo.entity.Permission.MANAGER_READ;
import static com.example.demo.entity.Permission.MANAGER_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	 USER(Collections.emptySet()),
	  ADMIN(
	          Set.of(
	                  ADMIN_READ,
	                  ADMIN_UPDATE,
	                  ADMIN_DELETE,
	                  ADMIN_CREATE,
	                  MANAGER_READ,
	                  MANAGER_UPDATE,
	                  MANAGER_DELETE,
	                  MANAGER_CREATE
	          )
	  ),
	  MANAGER(
	          Set.of(
	                  MANAGER_READ,
	                  MANAGER_UPDATE,
	                  MANAGER_DELETE,
	                  MANAGER_CREATE
	          )
	  )

	  ;
	  @Getter
	  private Set<Permission> permissions;
	  
	  Role(Set<Permission> permissions) {
	        this.permissions = permissions;
	    }

	  public List<SimpleGrantedAuthority> getAuthorities() {
	    var authorities = getPermissions()
	            .stream()
	            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
	            .collect(Collectors.toList());
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
	    return authorities;
	  }
}
