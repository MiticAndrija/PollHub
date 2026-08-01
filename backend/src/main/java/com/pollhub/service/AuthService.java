package com.pollhub.service;

import com.pollhub.dto.AuthResponse;
import com.pollhub.dto.LoginRequest;
import com.pollhub.dto.RegisterRequest;
import com.pollhub.dto.UserResponse;
import com.pollhub.entity.Role;
import com.pollhub.entity.User;
import com.pollhub.exception.DuplicateEmailException;
import com.pollhub.exception.InvalidCredentialsException;
import com.pollhub.exception.MissingRoleException;
import com.pollhub.repository.RoleRepository;
import com.pollhub.repository.UserRepository;
import com.pollhub.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        Role userRole = roleRepository.findByName(USER_ROLE)
                .orElseThrow(() -> new MissingRoleException(USER_ROLE));

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(new LinkedHashSet<>(Set.of(userRole)));

        try {
            return toUserResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new AuthResponse(jwtService.generateToken(userDetails), "Bearer", user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), roleNames(user));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), roleNames(user),
                user.getCreatedAt());
    }

    private Set<String> roleNames(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
