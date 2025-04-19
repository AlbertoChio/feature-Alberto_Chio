package com.example.aplazo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.aplazo.entity.User;

@Service
public class JwtService {
	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractUserUUID(String token) {
		final Claims claims = extractAllClaims(token);
		return claims.get("userId", String.class);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(User userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("userId", userDetails.getId());
		extraClaims.put("role", userDetails.getRole());
		String fullName = Optional.ofNullable(userDetails.getCustomer())
				.map(customer -> Stream
						.of(customer.getFirstName(), customer.getLastName(), customer.getSecondLastName())
						.filter(Objects::nonNull).collect(Collectors.joining(" ")))
				.orElse("Unknown");
		extraClaims.put("name", fullName);
		return generateToken(extraClaims, userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, User userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public long getExpirationTime() {
		return jwtExpiration;
	}

	private String buildToken(Map<String, Object> extraClaims, User userDetails, long expiration) {
		return Jwts.builder().claims(extraClaims).subject(Double.valueOf(1234567890).toString())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSignInKey(), Jwts.SIG.HS256)
				.compact();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenValid(String token, User userDetails) {
		final String userUUID = extractUserUUID(token);
		return (String.valueOf(userUUID).equals(String.valueOf(userDetails.getId()))) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

}