package com.divsis.money_manager.controller;

import com.divsis.money_manager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping
    public ResponseEntity<?> getDashboardData(){
        try{
            return ResponseEntity.ok(dashboardService.getDashboardData());
        }catch(Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
