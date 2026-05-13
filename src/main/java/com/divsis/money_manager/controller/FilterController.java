package com.divsis.money_manager.controller;

import com.divsis.money_manager.dto.FilterDTO;
import com.divsis.money_manager.service.ExpenseService;
import com.divsis.money_manager.service.IncomeService;
import io.netty.handler.codec.socks.SocksRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO){
        try {
           LocalDate startDate = filterDTO.getStartDate() !=null ? filterDTO.getStartDate(): LocalDate.MIN;
           LocalDate endDate = filterDTO.getEndDate() !=null ? filterDTO.getEndDate() : LocalDate.now();
           String keyword = filterDTO.getKeyword() !=null ? filterDTO.getKeyword() : "";
           String sortField = filterDTO.getSortField() !=null ? filterDTO.getSortField() : "date";
           Sort.Direction sortOrder = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
           Sort sort = Sort.by(sortOrder, sortField);
           if("income".equalsIgnoreCase(filterDTO.getType())){
               return ResponseEntity.ok(incomeService.filterIncomes(startDate,endDate,keyword,sort));
           }else if("expense".equalsIgnoreCase(filterDTO.getType())) {
               return ResponseEntity.ok(expenseService.filterExpenses(startDate, endDate, keyword, sort));
           }else{
               return ResponseEntity.badRequest().body("Invalid Type must be income or expense");
           }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
