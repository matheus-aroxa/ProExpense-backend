package com.mat.proexpense_backend.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mat.proexpense_backend.entitties.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findUserByEmail(String email);

  boolean existsByEmail(String email);
}
