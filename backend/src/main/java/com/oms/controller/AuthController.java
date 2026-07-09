package com.oms.controller;

import com.oms.dto.*;
import com.oms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
        return ResponseEntity.ok().build();
    }

    @Autowired
    private javax.sql.DataSource dataSource;

    @GetMapping("/db-ping")
    public ResponseEntity<java.util.Map<String, String>> healthCheck() {
        java.util.Map<String, String> response = new java.util.HashMap<>();
        try (java.sql.Connection connection = dataSource.getConnection();
             java.sql.Statement statement = connection.createStatement();
             java.sql.ResultSet rs = statement.executeQuery("SELECT 1")) {
            if (rs.next()) {
                response.put("status", "UP");
                response.put("database", "CONNECTED");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        response.put("status", "DOWN");
        response.put("database", "DISCONNECTED");
        return ResponseEntity.status(503).body(response);
    }
}
