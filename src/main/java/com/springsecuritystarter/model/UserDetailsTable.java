package com.springsecuritystarter.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity()
@Table(name = "user_table")
@Data
public class UserDetailsTable implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String pwd;

    @OneToMany(mappedBy = "userDetailsTable", fetch = FetchType.EAGER)
    Set<Authority> authorities;

    String role;

    public UserDetailsTable(String username, String pwd, String role) {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
    }

    public UserDetailsTable() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
