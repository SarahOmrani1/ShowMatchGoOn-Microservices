package com.exemple.user.controller;

import com.exemple.user.dto.AuthRequest;
import com.exemple.user.dto.AuthResponse;
import com.exemple.user.dto.UserDTO;
import com.exemple.user.entity.User;
import com.exemple.user.repository.UserRepository;
import com.exemple.user.security.JwtTokenProvider;
import com.exemple.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    // INSCRIPTION (POST http://localhost:8081/api/auth/register)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        UserDTO userDTO = UserDTO.builder()
                .username(authRequest.getUsername())
                .password(authRequest.getPassword())
                .email(authRequest.getEmail())
                .firstName(authRequest.getFirstName())
                .lastName(authRequest.getLastName())
                .phoneNumber(authRequest.getPhoneNumber())
                .build();

        userService.createUser(userDTO);

        // Connexion automatique après inscription
        return login(authRequest);
    }

    // CONNEXION (POST http://localhost:8081/api/auth/login)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();

        // Mettre à jour la date de dernière connexion
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Générer le Token JWT
        String token = jwtTokenProvider.generateToken(userDetails, user.getId(), user.getRole());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }
}
