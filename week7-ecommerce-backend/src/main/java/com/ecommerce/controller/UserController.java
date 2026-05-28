package com.ecommerce.controller;

import com.ecommerce.model.dto.UserDTO;
import com.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO dto) { return userService.register(dto); }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) { return userService.getById(id); }

    @GetMapping
    public List<UserDTO> getAll() { return userService.getAll(); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}