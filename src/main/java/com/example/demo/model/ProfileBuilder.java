package com.example.demo.model;

import java.time.LocalDate;
import java.util.UUID;

public final class ProfileBuilder {
    private ProfileBuilder() {
    }

    public static Profile defaultProfile(ProfileType type) {
        LocalDate issueDate = LocalDate.now();
        return Profile.builder()
                .uuid(UUID.randomUUID().toString())
                .type(type == null ? ProfileType.USER : type)
                .fullName("New Profile")
                .department("GENERAL")
                .title("Member")
                .issueDate(issueDate)
                .expiryDate(issueDate.plusYears(1))
                .barcodeType(BarcodeType.CODE_128)
                .build();
    }
}
