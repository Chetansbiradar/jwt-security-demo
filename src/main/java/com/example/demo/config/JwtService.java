package com.example.demo.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class JwtService {

	
	
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;
	
	//private static final String SECRET_KEY = "VhnOxhjINTa7e4C5BFjS60MjmILfMi50";
	
	public String extractUsername(String jwtToken)
	{
		return extractClaim(jwtToken, Claims::getSubject);
	}
	
	//Generic method
	public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) 
	{
		final Claims claims=extractAllClaims(jwtToken);
		return claimsResolver.apply(claims);
	}

	//to validate token
	public boolean isTokenValid(String token, UserDetails userDetails)
	{
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}
	
	public Date extractExpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	//==================
	public String generateToken(UserDetails userDetails)
	{
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
	{
		    return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails)
	{
		 return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
		          Map<String, Object> extraClaims,
		          UserDetails userDetails,
		          long expiration
		  ) 
    {
		    return Jwts
		            .builder()
		            .setClaims(extraClaims)
		            .setSubject(userDetails.getUsername())
		            .setIssuedAt(new Date(System.currentTimeMillis()))
		            .setExpiration(new Date(System.currentTimeMillis() + expiration))
		            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
		            .compact();
	}

	//===================
	/*
	public String generateToken(UserDetails userDetails)
	{
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
	{
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	*/
	private Claims extractAllClaims(String jwtToken)
	{
		
		//SignInKey is a secret tht is used to digitally sign the JWT. the SignInKey is used to create the signature part of jwt
		//which is used to verify the sender of jwt who it claims to be and ensure tht the msg wasnt changed along the way 
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
	}

	private Key getSignInKey() 
	{
		// TODO Auto-generated method stub
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
