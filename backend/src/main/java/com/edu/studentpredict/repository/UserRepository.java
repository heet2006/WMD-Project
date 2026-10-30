package com.edu.studentpredict.repository;

import com.edu.studentpredict.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByFirebaseUid(String firebaseUid);
    boolean existsByUsername(String username);
}
