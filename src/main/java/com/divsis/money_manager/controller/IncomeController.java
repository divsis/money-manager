package com.divsis.money_manager.controller;

import com.divsis.money_manager.dto.IncomeDTO;
import com.divsis.money_manager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping("/add")
    public ResponseEntity<?> addIncomes(@RequestBody IncomeDTO incomeDTO) {
        try{
            IncomeDTO saved = incomeService.addIncome(incomeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/get")
    public ResponseEntity<?> getExpenses(){
        try{
            List<IncomeDTO> expenses = incomeService.getCurrentMonthIncomes();
            return  ResponseEntity.status(HttpStatus.OK).body(expenses);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id){
        try{
            incomeService.deleteIncome(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Expense has been deleted");
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/get/{num}")
    public ResponseEntity<?> getNExpenses(@PathVariable int num){
        try
        {
            List<IncomeDTO> incomes = incomeService.getLatestNIncomes(num);
            return  ResponseEntity.status(HttpStatus.OK).body(incomes);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/get/total")
    public ResponseEntity<?> getTotalIncomes(){
        try{
            BigDecimal total = incomeService.getTotalIncome();
            return  ResponseEntity.status(HttpStatus.OK).body(total);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
