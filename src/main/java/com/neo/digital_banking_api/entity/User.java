package com.neo.digital_banking_api.entity;

import com.neo.digital_banking_api.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
//@Table(name = "users", uniqueConstraints = {
//    @UniqueConstraint(columnNames = "username"),
//    @UniqueConstraint(columnNames = "email")
//})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @NotBlank(message = "Name is required")
//    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Username is required")
//    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
//    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
//    @Column(name = "password", nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Enumerated(EnumType.STRING)
//    @Column(name = "role", nullable = false)
    private UserRole role;

//    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
