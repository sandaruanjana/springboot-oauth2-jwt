package com.example.resourceserverjwtauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity home(Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
