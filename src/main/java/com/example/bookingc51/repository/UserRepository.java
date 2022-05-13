package com.example.bookingc51.repository;

import com.example.bookingc51.entity.User;
import com.example.bookingc51.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User getById (long id);
    User getByUsername (String username);
    boolean existsByUsername(String username);
    List<User> findAllByRolesContains(UserRole role);
}
