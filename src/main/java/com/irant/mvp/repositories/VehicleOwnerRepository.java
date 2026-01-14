package com.irant.mvp.repositories;

import com.irant.mvp.models.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {
    Optional<VehicleOwner> findByUserId(Long userId);

    Boolean existsByUserId(Long userId);
}
