package com.irant.mvp.services;

import com.irant.mvp.dto.AgencyDto;
import com.irant.mvp.dto.AgencyRequest;
import com.irant.mvp.models.Agency;
import com.irant.mvp.models.User;
import com.irant.mvp.repositories.AgencyRepository;
import com.irant.mvp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;

    /**
     * Create agency for authenticated user
     * EPIC B2: Agency Management
     */
    public AgencyDto createAgency(AgencyRequest request, String userEmail) {
        log.info("Creating agency for user: {}", userEmail);

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        // Vérifier si l'utilisateur a déjà une agence
        if (agencyRepository.existsByUser(user)) {
            throw new IllegalStateException("User already has an agency");
        }

        // Vérifier si l'email de l'agence est déjà utilisé
        if (request.getEmail() != null && agencyRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Agency email already exists: " + request.getEmail());
        }

        // Créer l'agence
        Agency agency = Agency.builder()
                .name(request.getName())
                .email(request.getEmail() != null ? request.getEmail() : user.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .user(user)
                .build();

        Agency savedAgency = agencyRepository.save(agency);
        log.info("Agency created successfully: {}", savedAgency.getId());

        return mapToDto(savedAgency);
    }

    /**
     * Get agency by user email
     */
    public AgencyDto getAgencyByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        Agency agency = agencyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User does not have an agency"));

        return mapToDto(agency);
    }

    /**
     * Update agency profile
     * EPIC B2: Agency Management - Create agency profile update API
     * 
     * @param agencyId ID de l'agence à mettre à jour
     * @param request Données de mise à jour
     * @param userEmail Email de l'utilisateur connecté
     * @return AgencyDto mis à jour
     * @throws IllegalArgumentException si l'agence n'existe pas
     * @throws IllegalStateException si l'utilisateur ne possède pas cette agence (Check agency ownership)
     */
    public AgencyDto updateAgency(Long agencyId, AgencyRequest request, String userEmail) {
        log.info("Updating agency {} for user: {}", agencyId, userEmail);

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        // Récupérer l'agence
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agency not found with id: " + agencyId));

        // EPIC B2: Check agency ownership - Vérifier que l'utilisateur possède cette agence
        if (!agency.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("User does not own this agency. Access denied.");
        }

        // Vérifier si l'email de l'agence est déjà utilisé par une autre agence
        if (request.getEmail() != null && !request.getEmail().equals(agency.getEmail())) {
            if (agencyRepository.existsByEmail(request.getEmail())) {
                throw new IllegalStateException("Agency email already exists: " + request.getEmail());
            }
        }

        // Mettre à jour les champs fournis
        if (request.getName() != null) {
            agency.setName(request.getName());
        }
        if (request.getEmail() != null) {
            agency.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            agency.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getCity() != null) {
            agency.setCity(request.getCity());
        }

        Agency updatedAgency = agencyRepository.save(agency);
        log.info("Agency updated successfully: {}", updatedAgency.getId());

        return mapToDto(updatedAgency);
    }

    /**
     * Check if user owns the agency
     * EPIC B2: Check agency ownership
     * 
     * @param agencyId ID de l'agence
     * @param userEmail Email de l'utilisateur
     * @return true si l'utilisateur possède l'agence, false sinon
     */
    public boolean checkAgencyOwnership(Long agencyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agency not found with id: " + agencyId));

        return agency.getUser().getId().equals(user.getId());
    }

    /**
     * Map Agency to DTO
     */
    private AgencyDto mapToDto(Agency agency) {
        return AgencyDto.builder()
                .id(agency.getId())
                .name(agency.getName())
                .email(agency.getEmail())
                .phoneNumber(agency.getPhoneNumber())
                .city(agency.getCity())
                .userId(agency.getUser().getId())
                .createdAt(agency.getCreatedAt())
                .updatedAt(agency.getUpdatedAt())
                .build();
    }
}
