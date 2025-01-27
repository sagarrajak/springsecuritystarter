package com.springsecuritystarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecuritystarter.constants.JWTConstansts;
import com.springsecuritystarter.service.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", null);
        String password = body.getOrDefault("password", null);
        if (username != null && password != null) {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticated = authenticationManager.authenticate(authenticationToken);
            if (authenticated != null && authenticated.isAuthenticated())  {
                String s = this.jwtService.generateToken(authenticated);
                Map<String, String> response = new HashMap<>();
                response.put("token", JWTConstansts.TOKEN_PREFIX+s);
                return ResponseEntity.ok(response);
            } else {
                throw new BadCredentialsException("Username not provided invalid");
            }
        }
        throw new BadCredentialsException("Username not provided");
    }
}
