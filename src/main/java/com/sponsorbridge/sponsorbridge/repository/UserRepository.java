package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // for login — find user by email
    Optional<User> findByEmail(String email);

    // check if email already registered
    boolean existsByEmail(String email);

    // find all users by role
    List<User> findByRole(Role role);
}
