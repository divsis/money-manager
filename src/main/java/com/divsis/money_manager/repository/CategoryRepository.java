package com.divsis.money_manager.repository;

import com.divsis.money_manager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    //Select * from tbl_categories where profile_id =?
    List<CategoryEntity> findByProfileId(Long profileId);
    Optional<CategoryEntity> findByIdAndProfileId(Long id , Long profileId);
    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);
    Boolean existsByNameAndProfileId(String type, Long profileId);
}
