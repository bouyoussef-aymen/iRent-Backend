package com.irant.mvp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cars")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor 
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int year;
    
    // Utilisation de BigDecimal pour correspondre au DECIMAL(10,2) SQL
    @Column(name = "price_per_day")
    private BigDecimal pricePerDay; 
    
    private String transmission;
    
    @Column(name = "fuel_type")
    private String fuelType;
    
    @Column(name = "seating_capacity")
    private int seatingCapacity;
    
    private String color;
    
    @Column(name = "license_plate")
    private String licensePlate;
    
    @Column(name = "range_in_miles")
    private BigDecimal rangeInMiles;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;

    private String city;

    @ElementCollection
    @CollectionTable(name = "car_images", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonIgnoreProperties({"cars", "user"}) // Évite la sérialisation récursive
    private Agency agency;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}