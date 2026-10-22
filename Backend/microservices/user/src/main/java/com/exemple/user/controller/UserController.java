package com.exemple.user.controller;

import com.exemple.user.dto.UserDTO;
import com.exemple.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // 1. CRÉER UN UTILISATEUR (POST http://localhost:8081/api/users)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 2. RÉCUPÉRER TOUS LES UTILISATEURS (GET http://localhost:8081/api/users)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 3. RÉCUPÉRER UN UTILISATEUR PAR ID (GET http://localhost:8081/api/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. METTRE À JOUR UN UTILISATEUR (PUT http://localhost:8081/api/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    // 5. SUPPRIMER UN UTILISATEUR (DELETE http://localhost:8081/api/users/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 6. BLOQUER UN UTILISATEUR (POST http://localhost:8081/api/users/{id}/lock)
    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, String>> lockUser(@PathVariable Long id) {
        userService.lockUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User locked successfully");
        return ResponseEntity.ok(response);
    }

    // 7. DÉBLOQUER UN UTILISATEUR (POST http://localhost:8081/api/users/{id}/unlock)
    @PostMapping("/{id}/unlock")
    public ResponseEntity<Map<String, String>> unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User unlocked successfully");
        return ResponseEntity.ok(response);
    }

    // 8. VÉRIFIER DISPONIBILITÉ USERNAME (GET http://localhost:8081/api/users/check/username/{username})
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
        return ResponseEntity.ok(!userService.existsByUsername(username));
    }

    // 9. VÉRIFIER DISPONIBILITÉ EMAIL (GET http://localhost:8081/api/users/check/email/{email})
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> isEmailAvailable(@PathVariable String email) {
        return ResponseEntity.ok(!userService.existsByEmail(email));
    }
}
