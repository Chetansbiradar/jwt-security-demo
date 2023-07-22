package com.example.demo.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.token.Token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private String username;
	@Column(unique = true)
	@NotNull
	private String password;
	@Column
	@NotNull
	private String businessName;
	@Column
	@NotNull
	private String ownerName;
	@Column(unique = true, length = 10)
	@NotNull
	@Min(10)
	@Max(10)
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
	@Column(unique = true, length = 12)
	@NotNull
	@Min(12)
	@Max(12)
    @Pattern(regexp = "\\d{12}", message = "Aadhar number must be exactly 12 digits.")
    private String aadharNumber;
	@Column(nullable = false, length = 16)
	@Min(11)
	@Max(16)
	private int bankAccoutNo;
	@Column(unique=true, nullable = false, length=10)
	@Min(10)
	@Max(10)
	private String panCardNo;
	@Column(unique=true, nullable = false, length=14)
	@Min(14)
	@Max(14)
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
