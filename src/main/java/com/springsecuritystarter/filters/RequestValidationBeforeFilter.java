package com.springsecuritystarter.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RequestValidationBeforeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header1 = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header1 != null) {
            String header = req.getHeader(HttpHeaders.AUTHORIZATION).trim();
            if (StringUtils.startsWithIgnoreCase(header, "Basic ")) {
                try {
                    byte[] bytes = header.substring("Basic ".length()).getBytes(StandardCharsets.UTF_8);
                    byte[] decoded = Base64.getDecoder().decode(bytes);
                    String decodedString = new String(decoded, StandardCharsets.UTF_8);
                    int delimiter = decodedString.indexOf(":");
                    if (delimiter == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }
                    String username = decodedString.substring(0, delimiter);
                    if (username.toLowerCase().contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, String> responseMap = new HashMap<>();
                        responseMap.put("message", "Username dose not match");
                        String jsonResponse = objectMapper.writeValueAsString(responseMap);
                        res.getWriter().write(jsonResponse);
                        return;
                    }
                } catch (IOException | BadCredentialsException e) {
                    throw new BadCredentialsException("Unable to decode token");
                }
            }
        }
        chain.doFilter(request, response);
    }
}
