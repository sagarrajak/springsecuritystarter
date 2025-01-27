package com.springsecuritystarter.configuration;

import com.springsecuritystarter.filters.AuthoritiesLoggingFilter;
import com.springsecuritystarter.filters.JWTTokenGeneratorFilter;
import com.springsecuritystarter.filters.JWTTokenValidaterFilter;
import com.springsecuritystarter.filters.RequestValidationBeforeFilter;
import com.springsecuritystarter.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


// security configuration for jwt based authentication
@Configuration
@Profile("jwt")
public class SecurityConfigurationJWT {

   private final JWTTokenGeneratorFilter jwtTokenGeneratorFilter;
   private final JWTTokenValidaterFilter jwtTokenValidaterFilter;

    public SecurityConfigurationJWT(JWTTokenGeneratorFilter jwtTokenGeneratorFilter, JWTTokenValidaterFilter jwtTokenValidaterFilter) {
        this.jwtTokenGeneratorFilter = jwtTokenGeneratorFilter;
        this.jwtTokenValidaterFilter = jwtTokenValidaterFilter;
    }

    @Bean
    SecurityFilterChain defaultChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(s -> {
                    s.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
                            corsConfiguration.setAllowedMethods(Arrays.asList("PUT", "DELETE", "POST", "GET"));
                            corsConfiguration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
                            corsConfiguration.setAllowCredentials(true);
                            corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization")); // response header
                            corsConfiguration.setMaxAge(3600L);
                            return corsConfiguration;
                        }
                    });
                })
                .addFilterBefore(new AuthoritiesLoggingFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtTokenValidaterFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(cus -> {
                    cus.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                            .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                            .requestMatchers("/myBalance").hasAuthority("VIEWLOANS")
                            .requestMatchers("/myBalance").hasAuthority("VIEWBALANCE")
                            .requestMatchers("/myAdminPage").hasRole("ADMIN")
                            .requestMatchers("/myUserPage").hasRole("USER")
                            .requestMatchers("/myDashboard")
                            .authenticated();
                    cus.requestMatchers("/notices", "/contacts", "/error", "/auth/**").permitAll();
                });
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    PasswordEncoder getPasswordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationProvider bankAuthenticationProvider = new BankAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(bankAuthenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}
