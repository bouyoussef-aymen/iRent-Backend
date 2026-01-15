package com.irant.mvp.controllers;

import com.irant.mvp.models.Car;
import com.irant.mvp.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    // URL pour ajouter une voiture : POST /api/cars/add
    @PostMapping("/add")
    public ResponseEntity<Car> addCar(@RequestBody Car car, Authentication authentication) {
        // Récupérer l'email de l'utilisateur connecté
        String email = authentication.getName();
        return ResponseEntity.ok(carService.addCar(car, email));
    }

    // URL pour voir toutes les voitures : GET /api/cars/available
    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAllAvailableCars());
    }
}