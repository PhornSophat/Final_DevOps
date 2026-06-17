package com.example.demo.service;

import com.example.demo.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.Locale;

@Service
public class RegistrationNumberService {
    private final ProfileRepository profileRepository;

    public RegistrationNumberService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public String generate(String department) {
        String normalizedDepartment = StringUtils.hasText(department) ? department.trim().toUpperCase(Locale.ROOT) : "GEN";
        String code = normalizedDepartment.replaceAll("[^A-Z0-9]", "");
        if (code.length() > 4) {
            code = code.substring(0, 4);
        }
        if (code.isBlank()) {
            code = "GEN";
        }
        
        String prefix = Year.now().getValue() + "-" + code + "-";
        
        // Use the new repository method name we created
        long count = profileRepository.countByDepartmentAndRegistrationNumberPrefix(normalizedDepartment, prefix);
        
        int next = (int) (count + 1);
        String candidate = prefix + String.format("%03d", next);
        
        while (profileRepository.existsByRegistrationNumber(candidate)) {
            next++;
            candidate = prefix + String.format("%03d", next);
        }
        
        return candidate;
    }
}