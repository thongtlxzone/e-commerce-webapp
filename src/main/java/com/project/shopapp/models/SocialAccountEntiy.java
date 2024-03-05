package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "social_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccountEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false, length = 20)
    private String provider;
    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "email", length = 150)
    private String email;
}
