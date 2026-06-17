package com.example.demo.service;

import com.example.demo.model.Template;
import com.example.demo.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional(readOnly = true)
    public List<Template> list(String query) {
        if (StringUtils.hasText(query)) {
            return templateRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query);
        }
        return templateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Template get(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
    }

    public Template create(Template template) {
        if (templateRepository.existsByCode(template.getCode())) {
            throw new IllegalArgumentException("Template code already exists");
        }
        return templateRepository.save(template);
    }

    public Template update(Long id, Template update) {
        Template template = get(id);
        template.setCode(update.getCode());
        template.setName(update.getName());
        template.setOrganizationName(update.getOrganizationName());
        template.setLayout(update.getLayout());
        template.setPrimaryColor(update.getPrimaryColor());
        template.setSecondaryColor(update.getSecondaryColor());
        template.setTextColor(update.getTextColor());
        template.setTagline(update.getTagline());
        return templateRepository.save(template);
    }

    public void delete(Long id) {
        templateRepository.deleteById(id);
    }
}
