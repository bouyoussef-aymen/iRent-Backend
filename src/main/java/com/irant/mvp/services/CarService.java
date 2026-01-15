package com.irant.mvp.services;

import com.irant.mvp.models.Agency;
import com.irant.mvp.models.Car;
import com.irant.mvp.models.User;
import com.irant.mvp.repositories.AgencyRepository;
import com.irant.mvp.repositories.CarRepository;
import com.irant.mvp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;

    public Car addCar(Car car, String userEmail) {
        log.info("Adding car for user: {}", userEmail);
        
        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
        
        // Récupérer l'agence de l'utilisateur
        Agency agency = agencyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User does not have an agency. Please create an agency first."));
        
        // Assigner l'agence à la voiture
        car.setAgency(agency);
        
        // S'assurer que isAvailable n'est pas null
        if (car.getIsAvailable() == null) {
            car.setIsAvailable(true);
        }
        
        return carRepository.save(car);
    }

    public List<Car> getAllAvailableCars() {
        return carRepository.findByIsAvailableTrue();
    }

    /**
     * Get car by ID
     */
    public Car getCarById(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        // Forcer le chargement de imageUrls pour éviter les erreurs de lazy loading
        Hibernate.initialize(car.getImageUrls());
        return car;
    }

    /**
     * Update car - EPIC B3: Create update car API
     */
    public Car updateCar(Long carId, Car carUpdate, String userEmail) {
        log.info("Updating car {} for user: {}", carId, userEmail);
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Agency userAgency = agencyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User does not have an agency"));
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        
        // Vérifier que la voiture appartient à l'agence de l'utilisateur
        if (!car.getAgency().getId().equals(userAgency.getId())) {
            throw new IllegalStateException("You don't own this car");
        }
        
        // Mettre à jour les champs fournis
        if (carUpdate.getBrand() != null) car.setBrand(carUpdate.getBrand());
        if (carUpdate.getModel() != null) car.setModel(carUpdate.getModel());
        if (carUpdate.getYear() > 0) car.setYear(carUpdate.getYear());
        if (carUpdate.getPricePerDay() != null) car.setPricePerDay(carUpdate.getPricePerDay());
        if (carUpdate.getTransmission() != null) car.setTransmission(carUpdate.getTransmission());
        if (carUpdate.getFuelType() != null) car.setFuelType(carUpdate.getFuelType());
        if (carUpdate.getSeatingCapacity() > 0) car.setSeatingCapacity(carUpdate.getSeatingCapacity());
        if (carUpdate.getColor() != null) car.setColor(carUpdate.getColor());
        if (carUpdate.getLicensePlate() != null) car.setLicensePlate(carUpdate.getLicensePlate());
        if (carUpdate.getRangeInMiles() != null) car.setRangeInMiles(carUpdate.getRangeInMiles());
        if (carUpdate.getDescription() != null) car.setDescription(carUpdate.getDescription());
        if (carUpdate.getCity() != null) car.setCity(carUpdate.getCity());
        if (carUpdate.getImageUrls() != null) car.setImageUrls(carUpdate.getImageUrls());
        if (carUpdate.getIsAvailable() != null) car.setIsAvailable(carUpdate.getIsAvailable());
        
        return carRepository.save(car);
    }

    /**
     * Deactivate car - EPIC B3: Create deactivate car API
     */
    public Car deactivateCar(Long carId, String userEmail) {
        log.info("Deactivating car {} for user: {}", carId, userEmail);
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Agency userAgency = agencyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User does not have an agency"));
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        
        // Vérifier que la voiture appartient à l'agence de l'utilisateur
        if (!car.getAgency().getId().equals(userAgency.getId())) {
            throw new IllegalStateException("You don't own this car");
        }
        
        car.setIsAvailable(false);
        return carRepository.save(car);
    }

    /**
     * Get cars by agency - EPIC B3: Create list cars by agency API
     */
    public List<Car> getCarsByAgency(String userEmail) {
        log.info("Getting cars for agency of user: {}", userEmail);
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Agency agency = agencyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User does not have an agency"));
        
        return carRepository.findByAgency(agency);
    }
}
