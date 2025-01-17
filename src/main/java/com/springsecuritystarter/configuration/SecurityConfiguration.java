package com.springsecuritystarter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain defaultChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(cus -> {
            cus.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                    .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                    .requestMatchers("/myBalance").hasAuthority("VIEWLOANS")
                    .requestMatchers("/myBalance").hasAuthority("VIEWBALANCE")
                    .requestMatchers("/myAdminPage").hasRole("ADMIN")
                    .requestMatchers("/myUserPage").hasRole("USER")
                    .requestMatchers("/myDashboard").authenticated();
            cus.requestMatchers("/notices", "/contacts", "/error").permitAll();
        });
        http.formLogin(Customizer.withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
//
//    @Bean
//    UserDetailsService getUserDetails() {
//        UserDetails build = User.withUsername("sagar").password(getPasswordEncoder().encode("sagar123")).build();
//        UserDetails build1 = User.withUsername("admin").password("{noop}sagar123").build();
//        return new InMemoryUserDetailsManager(build1, build);
//    }

//    @Bean
//    UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if (!jdbcUserDetailsManager.userExists("sagar")) {
//            UserDetails user = User.builder().username("sagar").password(getPasswordEncoder().encode("skfhdjfbgkdfmbgmdfbgmdfng")).roles("ADMIN").build();
//            jdbcUserDetailsManager.createUser(user);
//        }
//        return jdbcUserDetailsManager;
//    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker checkComparmisedPassword() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
