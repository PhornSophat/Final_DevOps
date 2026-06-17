package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles",
        uniqueConstraints = @UniqueConstraint(name = "uk_profile_reg_number", columnNames = "registration_number"))
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @Column(name = "registration_number", nullable = false, unique = true, length = 64)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ProfileType type;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(length = 80)
    private String department;

    @Column(length = 120)
    private String title;

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @Column(length = 60)
    private String bloodGroup;

    private LocalDate dateOfBirth;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    @Column(length = 255)
    private String photoFileName;

    @Column(length = 60)
    private String photoContentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private Template template;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BarcodeType barcodeType = BarcodeType.CODE_128;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.issueDate == null) {
            this.issueDate = LocalDate.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Transient
    public boolean hasPhoto() {
        return photoFileName != null && !photoFileName.isBlank();
    }

    public static ProfileBuilderInternal builder() {
        return new ProfileBuilderInternal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public BarcodeType getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(BarcodeType barcodeType) {
        this.barcodeType = barcodeType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static final class ProfileBuilderInternal {
        private final Profile profile = new Profile();

        private ProfileBuilderInternal() {
        }

        public ProfileBuilderInternal uuid(String uuid) {
            profile.setUuid(uuid);
            return this;
        }

        public ProfileBuilderInternal registrationNumber(String registrationNumber) {
            profile.setRegistrationNumber(registrationNumber);
            return this;
        }

        public ProfileBuilderInternal type(ProfileType type) {
            profile.setType(type);
            return this;
        }

        public ProfileBuilderInternal fullName(String fullName) {
            profile.setFullName(fullName);
            return this;
        }

        public ProfileBuilderInternal department(String department) {
            profile.setDepartment(department);
            return this;
        }

        public ProfileBuilderInternal title(String title) {
            profile.setTitle(title);
            return this;
        }

        public ProfileBuilderInternal email(String email) {
            profile.setEmail(email);
            return this;
        }

        public ProfileBuilderInternal phone(String phone) {
            profile.setPhone(phone);
            return this;
        }

        public ProfileBuilderInternal bloodGroup(String bloodGroup) {
            profile.setBloodGroup(bloodGroup);
            return this;
        }

        public ProfileBuilderInternal dateOfBirth(LocalDate dateOfBirth) {
            profile.setDateOfBirth(dateOfBirth);
            return this;
        }

        public ProfileBuilderInternal issueDate(LocalDate issueDate) {
            profile.setIssueDate(issueDate);
            return this;
        }

        public ProfileBuilderInternal expiryDate(LocalDate expiryDate) {
            profile.setExpiryDate(expiryDate);
            return this;
        }

        public ProfileBuilderInternal template(Template template) {
            profile.setTemplate(template);
            return this;
        }

        public ProfileBuilderInternal barcodeType(BarcodeType barcodeType) {
            profile.setBarcodeType(barcodeType);
            return this;
        }

        public Profile build() {
            return profile;
        }
    }
}
