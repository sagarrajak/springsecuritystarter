package com.springsecuritystarter.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity()
@Table(name = "authorities")
@Getter
@Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetailsTable userDetailsTable;
}
