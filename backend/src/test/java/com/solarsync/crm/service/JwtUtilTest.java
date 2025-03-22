package com.solarsync.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    private JwtUtil jwtUtil;;
    private long expirationMillis;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        expirationMillis = 1000 * 60 * 60;
    }

    @Test
    void testGenerateAndValidateToken() {;
        String token = jwtUtil.generateToken("testuser", expirationMillis);
        String username = jwtUtil.extractUsername(token);
        assertNotNull(token);
        assertEquals("testuser", username);
        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        expirationMillis = 1500;
        String token = jwtUtil.generateToken("testuser", expirationMillis);
        Thread.sleep(expirationMillis + 5);
        assertTrue(jwtUtil.isTokenExpired(token), "Token should be expired");
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken),
                "Should throw an exception for invalid token");
        assertFalse(jwtUtil.validateToken(invalidToken, "testuser"),
                "Invalid token should not be validated");
    }

    @Test
    void testTamperedToken() {
        String token = jwtUtil.generateToken("testuser", expirationMillis);
        // Modify the token (simulate tampering)
        String tamperedToken = token.replace("a", "b");

        //assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(tamperedToken),
        //        "Should throw a signature exception for tampered token");
        assertFalse(jwtUtil.validateToken(tamperedToken, "testuser"),
                "Tampered token should be invalid");
    }

    @Test
    void testDifferentUserToken() {
        String token = jwtUtil.generateToken("user1", expirationMillis);

        assertFalse(jwtUtil.validateToken(token, "user2"),
                "Token should not be valid for a different user");
    }
}
