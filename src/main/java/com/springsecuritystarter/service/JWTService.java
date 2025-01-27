package com.springsecuritystarter.service;

import com.springsecuritystarter.configuration.JWTEnvironmentReader;
import com.springsecuritystarter.constants.JWTConstansts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private final JWTEnvironmentReader jwtEnvironmentReader;

    public JWTService(JWTEnvironmentReader jwtEnvironmentReader) {
        this.jwtEnvironmentReader = jwtEnvironmentReader;
    }

    public String generateToken(Authentication authentication) {
        Long expiration = this.jwtEnvironmentReader.getExpiration();
        String secret = this.jwtEnvironmentReader.getSecret();
        SecretKey secretKeyInBytes = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("Eazy bank")
                .setSubject("JWT token")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim(JWTConstansts.USERNAME, authentication.getName())
                .claim(JWTConstansts.AUTHORITIES, authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .signWith(secretKeyInBytes)
                .compact();
    }

    public boolean validateToken(String token) {
        String secret = this.jwtEnvironmentReader.getSecret();
        SecretKey secretKeyInBytes = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKeyInBytes)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return  false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        String secret = this.jwtEnvironmentReader.getSecret();
        SecretKey secretKeyInBytes = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKeyInBytes).build().parseClaimsJws(token);
        return claimsJws.getBody();
    }
}
