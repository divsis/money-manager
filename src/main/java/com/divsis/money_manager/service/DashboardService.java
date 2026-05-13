package com.divsis.money_manager.service;

import com.divsis.money_manager.dto.ExpenseDTO;
import com.divsis.money_manager.dto.IncomeDTO;
import com.divsis.money_manager.dto.RecentTransactionDTO;
import com.divsis.money_manager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String,Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String,Object> map = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatestNIncomes(5);
        List<ExpenseDTO> latestExpenses = expenseService.getLatestNExpenses(5);
        List<RecentTransactionDTO> recentTransactions = concat(
                latestIncomes.stream().map(income ->
                        RecentTransactionDTO.builder()
                                .id(income.getId())
                                .amount(income.getAmount())
                                .name(income.getName())
                                .icon(income.getIcon())
                                .date(income.getDate())
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdatedAt())
                                .type("income")
                                .build()
                ),
                latestExpenses.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .amount(expense.getAmount())
                                .name(expense.getName())
                                .icon(expense.getIcon())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()
                )
        )
        .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
        .collect(Collectors.toList());

        map.put("totalBalance", incomeService.getTotalIncome().subtract(expenseService.getTotalExpense()));
        map.put("totalIncome", incomeService.getTotalIncome());
        map.put("totalExpense", expenseService.getTotalExpense());
        map.put("recentTransactions", recentTransactions);
        map.put("latestIncomes", latestIncomes);
        map.put("latestExpenses", latestExpenses);
        return map;
    }
}
