package com.springsecuritystarter.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class AuthoritiesLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
           log.info("User {} is successfully authenticated with given {} authorities", authentication.getName(), authentication.getAuthorities().toString());
        }
        String servletPath = ((HttpServletRequest)request).getServletPath();
        System.out.println("Servlet Path: " + servletPath);
        chain.doFilter(request, response);
    }
}
