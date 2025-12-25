package com.ClothesMangement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 45)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('admin', 'staff') DEFAULT 'staff'")
    private Role role;

    public enum Role {
        admin, staff
    }
}
