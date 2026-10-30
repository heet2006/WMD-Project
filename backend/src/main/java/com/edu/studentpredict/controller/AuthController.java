package com.edu.studentpredict.controller;

import com.edu.studentpredict.dto.AuthDtos.*;
import com.edu.studentpredict.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication & Authorization", description = "Endpoints for Experiment 8: JWT Auth & Firebase Token Integration")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user with username and password, returns JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user with hashed password and role assignment")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/firebase-verify")
    @Operation(summary = "Verify Firebase ID Token and issue Spring Boot JWT session")
    public ResponseEntity<AuthResponse> verifyFirebase(@Valid @RequestBody FirebaseTokenRequest request) {
        AuthResponse response = authService.verifyAndSyncFirebaseUser(request);
        return ResponseEntity.ok(response);
    }
}
