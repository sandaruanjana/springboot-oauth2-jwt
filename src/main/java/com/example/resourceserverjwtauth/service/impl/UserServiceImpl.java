package com.example.resourceserverjwtauth.service.impl;

import com.example.resourceserverjwtauth.dao.RoleDao;
import com.example.resourceserverjwtauth.dao.UserDao;
import com.example.resourceserverjwtauth.dto.LoginDTO;
import com.example.resourceserverjwtauth.dto.SignupDTO;
import com.example.resourceserverjwtauth.dto.TokenDTO;
import com.example.resourceserverjwtauth.enums.RoleType;
import com.example.resourceserverjwtauth.exception.InternalServerErrorException;
import com.example.resourceserverjwtauth.model.Role;
import com.example.resourceserverjwtauth.model.User;
import com.example.resourceserverjwtauth.service.UserService;
import com.example.resourceserverjwtauth.utill.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;
    private final JwtUtils jwtUtils;

    @Override
    public boolean signup(SignupDTO signupDTO) {

        Role role = roleDao.findByName(RoleType.ROLE_USER.name());
        User user = new User();

        if (role != null) {
            user.setId(UUID.randomUUID().toString());
            user.setUsername(signupDTO.getUsername());
            user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
            user.setRole_id(role.getId());
        } else {
            return false;
        }

        return userDao.save(user);
    }

    @Override
    public TokenDTO login(Authentication authentication, HttpServletRequest request, LoginDTO loginDTO) {
        TokenDTO tokenDTO = new TokenDTO();
        try {
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String accessToken = jwtUtils.generateToken(authentication.getName(), request.getServletPath(), roles);
            String refreshToken = jwtUtils.generateRefreshToken(authentication.getName(), request.getServletPath(), roles);
            Long tokenExpirationTime = jwtUtils.getTokenExpirationTime(accessToken);

            tokenDTO.setAccess_token(accessToken);
            tokenDTO.setRefresh_token(refreshToken);
            tokenDTO.setExpires_in(tokenExpirationTime);
            tokenDTO.setToken_type("Bearer");

        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
        return tokenDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            String message = String.format("User %s not found", username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug("User {} found", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            Role userRole = roleDao.findById(user.getRole_id());

            if (userRole != null) {
                authorities.add(new SimpleGrantedAuthority(userRole.getName()));
            } else {
                throw new UsernameNotFoundException("User role not found");
            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }

    }
}
