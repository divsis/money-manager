package com.divsis.money_manager.controller;

import com.divsis.money_manager.dto.ExpenseDTO;
import com.divsis.money_manager.entity.CategoryEntity;
import com.divsis.money_manager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExpenses(@RequestBody ExpenseDTO expenseDTO){
        try{
            ExpenseDTO saved = expenseService.addExpense(expenseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getExpenses(){
        try{
            List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpenses();
            return  ResponseEntity.status(HttpStatus.OK).body(expenses);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id){
        try{
            expenseService.deleteExpense(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Expense has been deleted");
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/get/{num}")
    public ResponseEntity<?> getNExpenses(@PathVariable int num){
        try
        {
            List<ExpenseDTO> expenses = expenseService.getLatestNExpenses(num);
            return  ResponseEntity.status(HttpStatus.OK).body(expenses);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/get/total")
    public ResponseEntity<?> getTotalExpenses(){
        try{
            BigDecimal total = expenseService.getTotalExpense();
            return  ResponseEntity.status(HttpStatus.OK).body(total);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
