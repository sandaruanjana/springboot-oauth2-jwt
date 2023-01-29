package com.example.resourceserverjwtauth.controller;

import com.example.resourceserverjwtauth.dto.APIResponse;
import com.example.resourceserverjwtauth.dto.LoginDTO;
import com.example.resourceserverjwtauth.dto.SignupDTO;
import com.example.resourceserverjwtauth.dto.TokenDTO;
import com.example.resourceserverjwtauth.exception.InternalServerErrorException;
import com.example.resourceserverjwtauth.exception.UsernameAlreadyExistsException;
import com.example.resourceserverjwtauth.service.UserService;
import com.example.resourceserverjwtauth.utill.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * The signup function is used to register a new user.
     * It takes in the username, password, and email of the user as parameters,
     * and returns a response entity with the status code 201 if the user is registered successfully.
     *
     * @param @RequestBody Parse the json payload in the body of a request
     * @return Returns a response entity with the status code 201
     */
    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signup(@RequestBody @Valid SignupDTO signupDTO) {
        try {
            boolean isSignupSuccess = userService.signup(signupDTO);
            if (isSignupSuccess) {
                return ResponseEntity.created(null).build();
            }
            throw new InternalServerErrorException("Signup failed");
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException("Username already exists");
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong");
        }
    }

    /**
     * The login function is used to authenticate a user.
     * It takes in the username and password of the user as parameters,
     * and returns an access token if authentication is successful.
     *
     * @param @RequestBody Parse the json payload in the body of a request
     * @return Returns a response entity with the access token and refresh token
     */
    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (authentication.isAuthenticated()) {
                TokenDTO tokenDTO = userService.login(authentication, request, loginDTO);
                return ResponseEntity.ok(tokenDTO);
            }
        } catch (AuthenticationException e) {
            if (e.getMessage().equals("Bad credentials")) {
                throw new BadCredentialsException("Invalid username or password");
            }

            if (e.getMessage().equals("Incorrect result size: expected 1, actual 0")) {
                throw new BadCredentialsException("Invalid username or password");
            }

            throw new InternalServerErrorException("Something went wrong");
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong");
        }

        return ResponseEntity.badRequest().build();

    }

    /**
     * The refresh token function is used to generate a new access token.
     * It takes in the refresh token as a parameter,
     * and returns a new access token if the refresh token is valid.
     *
     * @param @RequestPart Parse the json payload in the body of a request
     * @return Returns a response entity with the access token
     */
    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestPart String refreshToken) {
        try {
            boolean tokenValid = jwtUtils.isTokenValid(refreshToken);
            if (tokenValid) {
                Map<String, Object> tokenClaim = jwtUtils.getTokenClaim(refreshToken);
                String accessToken = jwtUtils.generateToken((String) tokenClaim.get("sub"), (String) tokenClaim.get("iss"), (List<String>) tokenClaim.get("roles"));
                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("access_token", accessToken);
                return ResponseEntity.ok(tokenMap);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong");
        }
        return ResponseEntity.badRequest().build();
    }


}
