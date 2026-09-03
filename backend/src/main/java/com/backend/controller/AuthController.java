package com.backend.controller;

import com.backend.entity.Auth;
import com.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Auth> signin(@RequestBody Auth auth) {
        try {
            Auth signedIn = authService.signin(auth);
            return ResponseEntity.ok(signedIn);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<Auth> signout(@RequestBody Auth auth) {
        try {
            Auth signedOut = authService.signout(auth);
            return ResponseEntity.ok(signedOut);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<Auth> forgot(@RequestBody Auth auth) {
        try {
            Auth recovery = authService.forgot(auth);
            return ResponseEntity.ok(recovery);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Auth> reset(@RequestBody Auth auth) {
        try {
            Auth reseted = authService.reset(auth);
            return ResponseEntity.ok(reseted);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
