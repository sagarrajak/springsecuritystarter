package com.springsecuritystarter.filters;

import com.springsecuritystarter.constants.JWTConstansts;
import com.springsecuritystarter.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    public JWTTokenGeneratorFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String token = jwtService.generateToken(authentication);
            response.setHeader(JWTConstansts.HEADER_STRING, "Bearer " + token);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * @param request current HTTP request
     * @return
     * @throws ServletException
     */

}
