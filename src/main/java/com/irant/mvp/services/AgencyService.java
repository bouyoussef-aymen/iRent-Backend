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
