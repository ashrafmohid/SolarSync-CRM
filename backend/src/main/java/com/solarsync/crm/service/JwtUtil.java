package com.solarsync.crm.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "93buP+H/u/nZkku43N4F9GcYCp7auB1NoygfrW/3kq5I8QFnVOOSxzwLxu9Zw1Ub"; // Change in production

    // Generate JWT (Sign and return token)
    public String generateToken(String username , long expirationMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);  // Set username
        claims.put("iat", new Date());  // Set start time
        claims.put("exp", new Date(System.currentTimeMillis() + expirationMillis));  // Set end time
        return Jwts.builder()
                .claims(Jwts.claims().add(claims).build())  // Build claims
                .signWith(getSigningKey()) // Sign with secret key
                .compact(); // Generate the token
    }
    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract claims
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (SignatureException e) {
            return null; // Return null if tampering is detected
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .clockSkewSeconds(2)
                .verifyWith(getSigningKey()) // Use Secret Key for verification
                .build()
                .parseSignedClaims(token)  // Use parseSignedClaims for JWTs with signatures
                .getPayload();  // Extract Claims
        return claimsResolver.apply(claims);
    }

    // Validate signature and expiration
    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (SignatureException e) {
            return false; // Return false if token is tampers
        } catch (Exception e) {
            return false; // Return false for any other JWT parsing issue
        }
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
