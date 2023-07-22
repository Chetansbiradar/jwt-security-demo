package com.example.demo.entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.token.Token;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Business implements UserDetails {

	@Id
	@GeneratedValue
	private int id;
	@Column(unique = true)
	@NotNull
	private String username;
	@Column
	@NotNull
	private String password;
	@Column
	@NotNull
	private String businessName;
	@Column
	@NotNull
	private String ownerName;
	@Column(unique = true)
	@NotNull
	private int businessContactNumber;
	@Column
	@NotNull
	private String address;
	@Column
	@NotNull
	private String email;
	@Column
	@NotNull
	private String category;
	@Column(unique = true)
	@NotNull
//    @Pattern(regexp = "\\d{12}", message = "Aadhar number must be exactly 12 digits.")
    private String aadharNumber;
	@Column(nullable = false)
	private int bankAccoutNo;
	@Column(unique=true, nullable = false)
	private String panCardNo;
	@Column(unique=true, nullable = false)
	private String fssaiNumber;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(nullable = true)
	private String description;

	
	 @OneToMany(mappedBy = "user")
	  private List<Token> tokens;
	 
	// shld return a list of roles
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
//		return List.of(new SimpleGrantedAuthority(role.name()));
		return role.getAuthorities();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
		
	
}
