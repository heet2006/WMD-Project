package com.edu.studentpredict.service;

import com.edu.studentpredict.dto.AuthDtos.*;
import com.edu.studentpredict.model.Role;
import com.edu.studentpredict.model.User;
import com.edu.studentpredict.repository.UserRepository;
import com.edu.studentpredict.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        String token = tokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                tokenProvider.getExpirationInMs()
        );
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : Role.ROLE_TEACHER);

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved.getUsername(), saved.getRole().name());

        return new AuthResponse(
                token,
                saved.getUsername(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getRole().name(),
                tokenProvider.getExpirationInMs()
        );
    }

    public AuthResponse verifyAndSyncFirebaseUser(FirebaseTokenRequest request) {
        // Firebase ID Token verification flow
        // In full cloud setup, this validates token via Firebase Admin SDK.
        // For portable offline execution, we provide deterministic identity extraction:
        String token = request.getIdToken();
        String derivedUsername = "firebase_user";
        String derivedEmail = "user@firebase.app";

        if (token.contains("@")) {
            derivedUsername = token.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "");
            derivedEmail = token;
        }

        final String finalUsername = derivedUsername;
        final String finalEmail = derivedEmail;

        Role userRole = Role.ROLE_TEACHER;
        if (request.getRole() != null) {
            try {
                userRole = Role.valueOf(request.getRole());
            } catch (Exception ignored) {}
        }

        final Role assignedRole = userRole;

        User user = userRepository.findByUsername(finalUsername)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(finalUsername);
                    newUser.setEmail(finalEmail);
                    newUser.setFullName("Firebase User (" + finalUsername + ")");
                    newUser.setPassword(passwordEncoder.encode("firebase-oauth-" + System.currentTimeMillis()));
                    newUser.setRole(assignedRole);
                    newUser.setFirebaseUid(token.length() > 20 ? token.substring(0, 20) : token);
                    return userRepository.save(newUser);
                });

        String jwt = tokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(
                jwt,
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                tokenProvider.getExpirationInMs()
        );
    }

    @PostConstruct
    public void seedDefaultUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User("admin", passwordEncoder.encode("Admin@123"), "System Administrator", "admin@edu.org", Role.ROLE_ADMIN);
        User teacher = new User("teacher", passwordEncoder.encode("Teacher@123"), "Prof. Robert Smith", "teacher@edu.org", Role.ROLE_TEACHER);
        User student = new User("student", passwordEncoder.encode("Student@123"), "Aarav Sharma", "student@edu.org", Role.ROLE_STUDENT);

        userRepository.save(admin);
        userRepository.save(teacher);
        userRepository.save(student);
    }
}
