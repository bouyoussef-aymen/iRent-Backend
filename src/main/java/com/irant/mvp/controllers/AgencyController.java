package com.irant.mvp.controllers;

import com.irant.mvp.dto.AgencyDto;
import com.irant.mvp.dto.AgencyRequest;
import com.irant.mvp.services.AgencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;

    /**
     * Create agency for authenticated user
     * POST /api/agencies
     * EPIC B2: Agency Management
     */
    @PostMapping
    public ResponseEntity<AgencyDto> createAgency(@RequestBody AgencyRequest request, Authentication authentication) {
        log.info("Create agency request for user: {}", authentication.getName());
        try {
            String email = authentication.getName();
            AgencyDto agencyDto = agencyService.createAgency(request, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(agencyDto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("Agency creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get current user's agency
     * GET /api/agencies/me
     */
    @GetMapping("/me")
    public ResponseEntity<AgencyDto> getMyAgency(Authentication authentication) {
        try {
            String email = authentication.getName();
            AgencyDto agencyDto = agencyService.getAgencyByUserEmail(email);
            return ResponseEntity.ok(agencyDto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("Failed to get agency: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
