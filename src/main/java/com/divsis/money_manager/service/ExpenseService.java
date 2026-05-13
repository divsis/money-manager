package com.divsis.money_manager.service;

import com.divsis.money_manager.dto.ExpenseDTO;
import com.divsis.money_manager.entity.CategoryEntity;
import com.divsis.money_manager.entity.ExpenseEntity;
import com.divsis.money_manager.entity.ProfileEntity;
import com.divsis.money_manager.repository.CategoryRepository;
import com.divsis.money_manager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profile= profileService.getCurrentProfile();
        CategoryEntity category =categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category with id "+expenseDTO.getCategoryId()+" not found"));
        ExpenseEntity expense= toEntity(expenseDTO,profile,category);
        expense = expenseRepository.save(expense);
        return toDTO(expense);
    }
    public List<ExpenseDTO> getCurrentMonthExpenses(){
        ProfileEntity profile= profileService.getCurrentProfile();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
        List<ExpenseEntity>expenseEntities = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return expenseEntities.stream().map(this::toDTO).toList();
    }
    public void deleteExpense(Long expenseId){
        ProfileEntity profile= profileService.getCurrentProfile();
        ExpenseEntity expense= expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException("Expense with id "+expenseId+" not found"));
        if(expense.getProfile().getId().equals(profile.getId())){
            expenseRepository.delete(expense);
        }else {
            throw new RuntimeException("Unauthorized to Delete this Expense");
        }
    }
    public List<ExpenseDTO> getLatestNExpenses(int number){
        ProfileEntity profile= profileService.getCurrentProfile();
        List<ExpenseEntity>expenseEntities = expenseRepository.findByProfileIdOrderByDateDesc(profile.getId(), Pageable.ofSize(number));
        return expenseEntities.stream().map(this::toDTO).toList();
    }
    public BigDecimal getTotalExpense(){
        ProfileEntity profile= profileService.getCurrentProfile();
        BigDecimal totalExpense= expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return totalExpense!=null?totalExpense:BigDecimal.ZERO;
    }
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository
                .findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return expenses.stream().map(this::toDTO).toList();
    }
    public List<ExpenseDTO> getExpensesforUserOnDate(Long profileId, LocalDate date) {
        return expenseRepository
                .findByProfileIdAndDate(profileId, date).stream()
                .map(this::toDTO).toList();
    }


    //helper methods
    private ExpenseEntity toEntity(ExpenseDTO expenseDTO , ProfileEntity profileEntity , CategoryEntity categoryEntity){
        return ExpenseEntity.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }
    private ExpenseDTO toDTO(ExpenseEntity expenseEntity){
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .date(expenseEntity.getDate())
                .amount(expenseEntity.getAmount())
                .categoryId(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getId():null)
                .categoryName(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getName():null)
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }
}
