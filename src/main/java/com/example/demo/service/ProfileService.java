package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileBuilder;
import com.example.demo.model.ProfileType;
import com.example.demo.model.Template;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final TemplateRepository templateRepository;
    private final RegistrationNumberService registrationNumberService;
    private final PhotoStorageService photoStorageService;

    public ProfileService(ProfileRepository profileRepository,
                          TemplateRepository templateRepository,
                          RegistrationNumberService registrationNumberService,
                          PhotoStorageService photoStorageService) {
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
        this.registrationNumberService = registrationNumberService;
        this.photoStorageService = photoStorageService;
    }

    @Transactional(readOnly = true)
    public List<Profile> list(String query, ProfileType type) {
        if (type != null) {
            return profileRepository.findByType(type);
        }
        if (StringUtils.hasText(query)) {
            return profileRepository.findByFullNameContainingIgnoreCaseOrRegistrationNumberContainingIgnoreCase(query, query);
        }
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profile get(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));
    }

    @Transactional(readOnly = true)
    public Profile getByUuid(String uuid) {
        return profileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + uuid));
    }

    public Profile create(Profile profile, Long templateId, MultipartFile photo) {
        if (!StringUtils.hasText(profile.getUuid())) {
            profile.setUuid(UUID.randomUUID().toString());
        }
        if (profile.getType() == null) {
            profile.setType(ProfileType.USER);
        }
        if (!StringUtils.hasText(profile.getRegistrationNumber())) {
            profile.setRegistrationNumber(registrationNumberService.generate(profile.getDepartment()));
        }
        attachTemplate(profile, templateId);
        attachPhoto(profile, photo);
        return profileRepository.save(profile);
    }

    public Profile createDefault(ProfileType type) {
        Profile profile = ProfileBuilder.defaultProfile(type);
        profile.setRegistrationNumber(registrationNumberService.generate(profile.getDepartment()));
        return profileRepository.save(profile);
    }

    public Profile update(Long id, Profile update, Long templateId, MultipartFile photo) {
        Profile profile = get(id);
        profile.setFullName(update.getFullName());
        profile.setType(update.getType());
        profile.setDepartment(update.getDepartment());
        profile.setTitle(update.getTitle());
        profile.setEmail(update.getEmail());
        profile.setPhone(update.getPhone());
        profile.setBloodGroup(update.getBloodGroup());
        profile.setDateOfBirth(update.getDateOfBirth());
        profile.setIssueDate(update.getIssueDate());
        profile.setExpiryDate(update.getExpiryDate());
        profile.setBarcodeType(update.getBarcodeType());
        if (StringUtils.hasText(update.getRegistrationNumber())
                && !update.getRegistrationNumber().equals(profile.getRegistrationNumber())) {
            if (profileRepository.existsByRegistrationNumber(update.getRegistrationNumber())) {
                throw new IllegalArgumentException("Registration number already exists");
            }
            profile.setRegistrationNumber(update.getRegistrationNumber());
        }
        attachTemplate(profile, templateId);
        attachPhoto(profile, photo);
        return profileRepository.save(profile);
    }

    public void delete(Long id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> batchCreate(List<Profile> profiles) {
        return profiles.stream()
                .map(profile -> create(profile, profile.getTemplate() == null ? null : profile.getTemplate().getId(), null))
                .toList();
    }

    private void attachTemplate(Profile profile, Long templateId) {
        if (templateId == null) {
            return;
        }
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
        profile.setTemplate(template);
    }

    private void attachPhoto(Profile profile, MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return;
        }
        PhotoStorageService.StoredPhoto storedPhoto = photoStorageService.store(photo);
        profile.setPhotoFileName(storedPhoto.fileName());
        profile.setPhotoContentType(storedPhoto.contentType());
    }
}
