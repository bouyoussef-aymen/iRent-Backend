package com.irant.mvp.services;

import com.irant.mvp.dto.LoginRequest;
import com.irant.mvp.dto.LoginResponse;
import com.irant.mvp.dto.RegisterRequest;
import com.irant.mvp.dto.UserDto;
import com.irant.mvp.constants.UserType;
import com.irant.mvp.models.Renter;
import com.irant.mvp.models.Role;
import com.irant.mvp.models.User;
import com.irant.mvp.models.VehicleOwner;
import com.irant.mvp.repositories.RenterRepository;
import com.irant.mvp.repositories.RoleRepository;
import com.irant.mvp.repositories.UserRepository;
import com.irant.mvp.repositories.VehicleOwnerRepository;
import com.irant.mvp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RenterRepository renterRepository;
    private final VehicleOwnerRepository vehicleOwnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional // Ensures atomicity: if profile save fails, user isn't created
    public UserDto registerUser(RegisterRequest request) {
        log.info("Registering user: {}", request.getEmail());

        validateRegistrationRequest(request);

        // 1. Check Uniqueness (Standard RuntimeException or IllegalStateException)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email " + request.getEmail() + " is already taken.");
        }

        if (request.getPhoneNumber() != null && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalStateException("Phone number is already registered.");
        }

        // 2. Build User
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .country(request.getCountry())
                .city(request.getCity())
                .accountActive(true)
                .roles(new HashSet<>())
                .build();

        // 3. Logic: All users (including null type) get RENTER role/profile
        Role renterRole = roleRepository.findByName(Role.RoleName.ROLE_RENTER)
                .orElseThrow(() -> new NoSuchElementException("System Error: ROLE_RENTER not initialized in DB"));

        user.getRoles().add(renterRole);

        // 4. If they specifically asked for VEHICLE_OWNER, add it
        if (UserType.VEHICLE_OWNER.equals(request.getUserType())) {
            Role ownerRole = roleRepository.findByName(Role.RoleName.ROLE_VEHICLE_OWNER)
                    .orElseThrow(
                            () -> new NoSuchElementException("System Error: ROLE_VEHICLE_OWNER not initialized in DB"));
            user.getRoles().add(ownerRole);
        }

        // 5. Save User first (to get the ID for foreign keys)
        User savedUser = userRepository.save(user);

        // 6. Create Profiles
        renterRepository.save(new Renter(savedUser));
        log.info("Renter profile created for: {}", savedUser.getId());

        if (user.getRoles().stream().anyMatch(r -> r.getName() == Role.RoleName.ROLE_VEHICLE_OWNER)) {
            vehicleOwnerRepository.save(new VehicleOwner(savedUser));
            log.info("Vehicle Owner profile created for: {}", savedUser.getId());
        }

        return mapUserToDto(savedUser);
    }

    /**
     * Login user and return JWT token
     */
    public LoginResponse loginUser(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        try {
            // Authenticate user using email and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            // Get user from database
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            userRepository.save(user);

            // Generate tokens
            String accessToken = tokenProvider.generateTokenFromUserId(user.getId());

            log.info("User logged in successfully: {}", user.getId());

            return LoginResponse.builder()
                    .token(accessToken)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .roles(user.getRoles().stream()
                            .map(r -> r.getName().toString())
                            .collect(Collectors.toSet()))
                    .expiresIn(tokenProvider.getJwtExpirationMs())
                    .build();

        } catch (AuthenticationException e) {
            log.warn("Authentication failed for email: {}", request.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    /**
     * Get user profile by email
     */
    public UserDto getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return mapUserToDto(user);
    }
    
    /**
     * Validate registration request
     */
    private void validateRegistrationRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }

    /**
     * Map user to DTO
     */
    private UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .country(user.getCountry())
                .city(user.getCity())
                .accountActive(user.getAccountActive())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().toString())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
