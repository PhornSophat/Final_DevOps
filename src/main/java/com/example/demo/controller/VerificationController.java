package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {
    private final ProfileService profileService;

    public VerificationController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/verify/{uuid}")
    public Profile verify(@PathVariable String uuid) {
        return profileService.getByUuid(uuid);
    }
}
