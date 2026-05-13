package com.divsis.money_manager.service;

import com.divsis.money_manager.dto.IncomeDTO;
import com.divsis.money_manager.entity.CategoryEntity;
import com.divsis.money_manager.entity.IncomeEntity;
import com.divsis.money_manager.entity.ProfileEntity;
import com.divsis.money_manager.repository.CategoryRepository;
import com.divsis.money_manager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
       ProfileEntity profile= profileService.getCurrentProfile();
       CategoryEntity category =categoryRepository.findById(incomeDTO.getCategoryId())
               .orElseThrow(()->new RuntimeException("Category with id "+incomeDTO.getCategoryId()+" not found"));
       IncomeEntity incomeEntity = toEntity(incomeDTO,profile,category);
       incomeEntity = incomeRepository.save(incomeEntity);
       return toDTO(incomeEntity);
    }
    public List<IncomeDTO> getCurrentMonthIncomes(){
        ProfileEntity profile= profileService.getCurrentProfile();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return incomeEntities.stream().map(this::toDTO).toList();
    }
    public void deleteIncome(Long incomeId){
        ProfileEntity profile= profileService.getCurrentProfile();
        IncomeEntity income= incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("income with id "+incomeId+" not found"));
        if(income.getProfile().getId().equals(profile.getId())){
            incomeRepository.delete(income);
        }else {
            throw new RuntimeException("Unauthorized to Delete this income");
        }
    }
    public List<IncomeDTO> getLatestNIncomes(int number){
        ProfileEntity profile= profileService.getCurrentProfile();
        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdOrderByDateDesc(profile.getId(), Pageable.ofSize(number));
        return incomeEntities.stream().map(this::toDTO).toList();
    }
    public BigDecimal getTotalIncome(){
        ProfileEntity profile= profileService.getCurrentProfile();
        BigDecimal totalIncome= incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return totalIncome!=null?totalIncome:BigDecimal.ZERO;
    }
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            profile.getId(), startDate, endDate, keyword, sort
        );
        return incomes.stream().map(this::toDTO).toList();
    }


    //helper methods
    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity , CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .id(incomeDTO.getId())
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .date(incomeDTO.getDate())
                .amount(incomeDTO.getAmount())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }
    private IncomeDTO toDTO(IncomeEntity incomeEntity){
        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .date(incomeEntity.getDate())
                .amount(incomeEntity.getAmount())
                .categoryId(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getId():null)
                .categoryName(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getName():null)
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}
