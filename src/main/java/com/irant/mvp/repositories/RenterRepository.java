package com.irant.mvp.repositories;

import com.irant.mvp.models.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<Renter, Long> {
    Optional<Renter> findByUserId(Long userId);

    Boolean existsByUserId(Long userId);
}
