package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    public static String ADMIN = "ADMIN";
    public static String USER = "USER";

}
