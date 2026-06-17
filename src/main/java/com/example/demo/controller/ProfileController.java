package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import com.example.demo.service.CardRenderService;
import com.example.demo.service.CodeImageService;
import com.example.demo.service.PhotoStorageService;
import com.example.demo.service.ProfileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequestMapping
public class ProfileController {
    private final ProfileService profileService;
    private final PhotoStorageService photoStorageService;
    private final CardRenderService cardRenderService;
    private final CodeImageService codeImageService;

    public ProfileController(ProfileService profileService,
                             PhotoStorageService photoStorageService,
                             CardRenderService cardRenderService,
                             CodeImageService codeImageService) {
        this.profileService = profileService;
        this.photoStorageService = photoStorageService;
        this.cardRenderService = cardRenderService;
        this.codeImageService = codeImageService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("profiles", profileService.list(null, null));
        return "profiles";
    }

    @GetMapping("/profiles/{id}/preview")
    public String preview(@PathVariable Long id, Model model) {
        Profile profile = profileService.get(id);
        model.addAttribute("profile", profile);
        model.addAttribute("verificationUrl", codeImageService.verificationUrl(profile));
        return "id-card";
    }

    @GetMapping("/api/profiles")
    @ResponseBody
    public List<Profile> list(@RequestParam(required = false) String query,
                              @RequestParam(required = false) ProfileType type) {
        return profileService.list(query, type);
    }

    @GetMapping("/api/profiles/{id}")
    @ResponseBody
    public Profile get(@PathVariable Long id) {
        return profileService.get(id);
    }

    @PostMapping(value = "/api/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Profile create(@ModelAttribute Profile profile,
                          @RequestParam(required = false) Long templateId,
                          @RequestParam(required = false) MultipartFile photo) {
        return profileService.create(profile, templateId, photo);
    }

    @PostMapping("/api/profiles/default")
    @ResponseBody
    public Profile createDefault(@RequestParam(defaultValue = "USER") ProfileType type) {
        return profileService.createDefault(type);
    }

    @PutMapping(value = "/api/profiles/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Profile update(@PathVariable Long id,
                          @ModelAttribute Profile profile,
                          @RequestParam(required = false) Long templateId,
                          @RequestParam(required = false) MultipartFile photo) {
        return profileService.update(id, profile, templateId, photo);
    }

    @DeleteMapping("/api/profiles/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }

    @PostMapping("/api/profiles/batch")
    @ResponseBody
    public List<Profile> batch(@RequestBody List<Profile> profiles) {
        return profileService.batchCreate(profiles);
    }

    @GetMapping("/api/profiles/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Profile profile = profileService.get(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=id-card-" + profile.getRegistrationNumber() + ".pdf")
                .body(cardRenderService.pdf(profile));
    }

    @GetMapping("/api/profiles/{id}/qr")
    public ResponseEntity<byte[]> qr(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(codeImageService.qrCode(profileService.get(id)));
    }

    @GetMapping("/api/profiles/{id}/barcode")
    public ResponseEntity<byte[]> barcode(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(codeImageService.barcode(profileService.get(id)));
    }

    @GetMapping("/photos/{fileName}")
    public ResponseEntity<Resource> photo(@PathVariable String fileName) throws MalformedURLException {
        Resource resource = new UrlResource(photoStorageService.load(fileName).toUri());
        return ResponseEntity.ok().body(resource);
    }
}
