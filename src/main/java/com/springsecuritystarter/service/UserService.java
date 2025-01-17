package com.springsecuritystarter.service;

import com.springsecuritystarter.model.UserDetailsTable;
import com.springsecuritystarter.repository.UserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;

    public UserService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsTable userDetailsTable = this.userDetailsRepository
                .findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        return userDetailsTable;
    }
}
