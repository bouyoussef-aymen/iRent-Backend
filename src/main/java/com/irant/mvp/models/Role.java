package com.irant.mvp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName name;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Role(RoleName name) {
        this.name = name;
    }

    public enum RoleName {
        ROLE_RENTER,
        ROLE_VEHICLE_OWNER,
        ROLE_SUPER_ADMIN
    }
}
