package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProfileServiceTests {
    @Autowired
    private ProfileService profileService;

    @Test
    void createsProfileWithGeneratedUuidAndRegistrationNumber() {
        Profile profile = Profile.builder()
                .fullName("Sophea Chan")
                .type(ProfileType.STUDENT)
                .department("IT")
                .title("Year 3")
                .build();

        Profile saved = profileService.create(profile, null, null);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUuid()).isNotBlank();
        assertThat(saved.getRegistrationNumber()).startsWith(Year.now().getValue() + "-IT-");
    }
}
