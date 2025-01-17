package com.springsecuritystarter.repository;

import com.springsecuritystarter.model.UserDetailsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsTable, Long> {

    @Query("""
            select
                    case when count(u) > 0 then true else false end
                    from UserDetailsTable u where lower(trim(u.username)) = trim(lower(?1))
        """)
    boolean existsByUsername(String username);

    Optional<UserDetailsTable> findFirstByUsername(String username);
}
