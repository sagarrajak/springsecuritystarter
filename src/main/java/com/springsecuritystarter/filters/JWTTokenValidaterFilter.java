package com.springsecuritystarter.filters;

import com.springsecuritystarter.constants.JWTConstansts;
import com.springsecuritystarter.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTTokenValidaterFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    public JWTTokenValidaterFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JWTConstansts.HEADER_STRING);
        if (token != null && token.startsWith(JWTConstansts.TOKEN_PREFIX) ) {
            String tokenWithoutPrefix = token.replace(JWTConstansts.TOKEN_PREFIX, "");
            try {
                boolean ifValid = jwtService.validateToken(tokenWithoutPrefix);
                if (!ifValid)  throw new BadCredentialsException("Invalid JWT token");
                Claims claimsFromToken = jwtService.getClaimsFromToken(tokenWithoutPrefix);
                String username = String.valueOf(claimsFromToken.get(JWTConstansts.USERNAME));
                String authorities = String.valueOf(claimsFromToken.get(JWTConstansts.AUTHORITIES));
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token");
            }
        }
        filterChain.doFilter(request, response);
    }


}
