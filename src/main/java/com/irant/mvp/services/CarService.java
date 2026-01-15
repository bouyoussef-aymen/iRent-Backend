package com.irant.mvp.services;

import com.irant.mvp.models.Agency;
import com.irant.mvp.models.Car;
import com.irant.mvp.models.User;
import com.irant.mvp.repositories.AgencyRepository;
import com.irant.mvp.repositories.CarRepository;
import com.irant.mvp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
