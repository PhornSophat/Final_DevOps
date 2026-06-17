package com.example.demo.repository;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUuid(String uuid);
    Optional<Profile> findByRegistrationNumber(String registrationNumber);
    boolean existsByRegistrationNumber(String registrationNumber);
    List<Profile> findByType(ProfileType type);
    List<Profile> findByDepartmentIgnoreCase(String department);
    List<Profile> findByFullNameContainingIgnoreCaseOrRegistrationNumberContainingIgnoreCase(String name, String registrationNumber);

    // Custom query to bypass Hibernate's automatic ESCAPE generation
    @Query("SELECT COUNT(p) FROM Profile p WHERE UPPER(p.department) = UPPER(:department) AND p.registrationNumber LIKE CONCAT(:prefix, '%')")
    long countByDepartmentAndRegistrationNumberPrefix(@Param("department") String department, @Param("prefix") String prefix);
}