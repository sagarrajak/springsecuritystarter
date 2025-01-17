package com.springsecuritystarter.configuration;

import com.springsecuritystarter.model.UserDetailsTable;
import com.springsecuritystarter.repository.UserDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserDetailsRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("sagarrajak")) {
            userRepository.save(new UserDetailsTable("sagarrajak", passwordEncoder.encode("skdfdsffdsfds"), ""));
        }
    }
}
