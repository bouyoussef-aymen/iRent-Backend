package com.irant.mvp.repositories;

import com.irant.mvp.models.Agency;
import com.irant.mvp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    
    // Trouver une agence par email
    Optional<Agency> findByEmail(String email);
    
    // Trouver une agence par utilisateur (EPIC B2: Link agency to user account)
    Optional<Agency> findByUser(User user);
    
    // Vérifier si une agence existe pour un utilisateur
    boolean existsByUser(User user);
    
    // Vérifier si un email d'agence existe déjà
    boolean existsByEmail(String email);
}
