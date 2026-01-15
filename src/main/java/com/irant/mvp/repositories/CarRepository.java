package com.irant.mvp.repositories;

import com.irant.mvp.models.Agency;
import com.irant.mvp.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    
    // Trouver les voitures par agence
    List<Car> findByAgency(Agency agency);
    
    // Trouver les voitures par email de l'agence (via la relation agency)
    List<Car> findByAgency_Email(String email);

    // Trouver les voitures disponibles
    List<Car> findByIsAvailableTrue();
    
    // Trouver les voitures disponibles dans une ville
    List<Car> findByIsAvailableTrueAndCity(String city);
}