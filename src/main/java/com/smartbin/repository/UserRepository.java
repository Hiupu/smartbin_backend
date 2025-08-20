package com.smartbin.repository;

import com.smartbin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.UserRole role);

    List<User> findByEnabled(Boolean enabled);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<User> searchUsers(String query);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByRole(User.UserRole role);

    long countByEnabled(Boolean enabled);
}
