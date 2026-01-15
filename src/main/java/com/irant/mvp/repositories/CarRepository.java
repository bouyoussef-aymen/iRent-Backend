package com.irant.mvp.repositories;

import com.irant.mvp.models.Agency;
import com.irant.mvp.models.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    
    // Trouver une voiture par ID avec son agence chargée
    // Note: imageUrls est chargé en EAGER via @ElementCollection dans le modèle
    @EntityGraph(attributePaths = {"agency"})
    @Override
    Optional<Car> findById(Long id);
    
    // Trouver les voitures par agence
    List<Car> findByAgency(Agency agency);
    
    // Trouver les voitures par email de l'agence (via la relation agency)
    List<Car> findByAgency_Email(String email);

    // Trouver les voitures disponibles
    List<Car> findByIsAvailableTrue();
    
    // Trouver les voitures disponibles dans une ville
    List<Car> findByIsAvailableTrueAndCity(String city);
}