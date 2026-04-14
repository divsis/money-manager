package com.divsis.money_manager.repository;

import com.divsis.money_manager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    //Search for a profile by email, return an Optional to handle the case where the profile may not exist
    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity>findByActivationToken(String activationToken);
}
