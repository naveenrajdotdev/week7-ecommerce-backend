package com.ecommerce.model.dto;

import com.ecommerce.model.enums.Role;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password; // used only for registration
    private Role role;
}