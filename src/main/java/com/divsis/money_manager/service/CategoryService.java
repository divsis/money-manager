package com.divsis.money_manager.service;

import com.divsis.money_manager.dto.CategoryDTO;
import com.divsis.money_manager.entity.CategoryEntity;
import com.divsis.money_manager.entity.ProfileEntity;
import com.divsis.money_manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("Category with name "+categoryDTO.getName()+" already exists");
        }
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be blank");
        }
        if (categoryDTO.getType() == null || categoryDTO.getType().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category type cannot be blank");
        }
        CategoryEntity categoryEntity = toEntity(categoryDTO,profile);
        categoryEntity = categoryRepository.save(categoryEntity);
        return toDTO(categoryEntity);
    }

    public List<CategoryDTO> getCategories(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categoryEntities = categoryRepository.findByProfileId(profile.getId());
        return categoryEntities.stream().map(this::toDTO).toList();
    }
    public List<CategoryDTO> getCategoriesFromType(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categoryEntities = categoryRepository.findByTypeAndProfileId(categoryDTO.getType(), profile.getId());
        return  categoryEntities.stream().map(this::toDTO).toList();
    }
    public CategoryDTO updateCategory(Long categoryId ,CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(()->new RuntimeException("Category with id "+categoryDTO.getId()+" not found"));
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setIcon(categoryDTO.getIcon());
        category.setUpdateDate(LocalDateTime.now());
        return toDTO(categoryRepository.save(category));
    }

    private CategoryEntity toEntity(CategoryDTO categoryDTO , ProfileEntity profileEntity){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .type(categoryDTO.getType())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .build();
    }
    private CategoryDTO toDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .name(categoryEntity.getName())
                .type(categoryEntity.getType())
                .icon(categoryEntity.getIcon())
                .profileId(categoryEntity.getProfile()!=null ? categoryEntity.getProfile().getId(): null)
                .creationDate(categoryEntity.getCreationDate())
                .updateDate(categoryEntity.getUpdateDate())
                .id(categoryEntity.getId())
                .build();
    }


}
