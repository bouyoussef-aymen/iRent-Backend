package com.irant.mvp.controllers;

import com.irant.mvp.models.Car;
import com.irant.mvp.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    /**
     * Add car
     * POST /api/cars/add
     */
    @PostMapping("/add")
    public ResponseEntity<Car> addCar(@RequestBody Car car, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(carService.addCar(car, email));
    }

    /**
     * Get all available cars
     * GET /api/cars/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAllAvailableCars());
    }

    /**
     * Get car by ID
     * GET /api/cars/{id}
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.ok(car);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update car
     * PUT /api/cars/{id}
     * EPIC B3: Create update car API
     */
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Car> updateCar(
            @PathVariable Long id,
            @RequestBody Car carUpdate,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Car updatedCar = carService.updateCar(id, carUpdate, email);
            return ResponseEntity.ok(updatedCar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Deactivate car
     * PATCH /api/cars/{id}/deactivate
     * EPIC B3: Create deactivate car API
     */
    @PatchMapping("/{id:\\d+}/deactivate")
    public ResponseEntity<Car> deactivateCar(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Car deactivatedCar = carService.deactivateCar(id, email);
            return ResponseEntity.ok(deactivatedCar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Get cars by agency
     * GET /api/cars/agency
     * EPIC B3: Create list cars by agency API
     */
    @GetMapping("/agency")
    public ResponseEntity<List<Car>> getCarsByAgency(Authentication authentication) {
        try {
            String email = authentication.getName();
            List<Car> cars = carService.getCarsByAgency(email);
            return ResponseEntity.ok(cars);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}