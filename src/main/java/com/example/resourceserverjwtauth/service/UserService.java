package com.example.resourceserverjwtauth.service;

import com.example.resourceserverjwtauth.dto.LoginDTO;
import com.example.resourceserverjwtauth.dto.SignupDTO;
import com.example.resourceserverjwtauth.dto.TokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public interface UserService {
    boolean signup(SignupDTO signupDTO);

    TokenDTO login(Authentication authentication, HttpServletRequest request, LoginDTO loginDTO);
}
