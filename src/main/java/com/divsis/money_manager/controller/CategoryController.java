package com.divsis.money_manager.controller;

import com.divsis.money_manager.dto.CategoryDTO;
import com.divsis.money_manager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO){
        try{
            CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/get")
    public ResponseEntity<?> getCategories(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/get/{type}")
    public ResponseEntity<?> getCategoriesByType(@PathVariable String type){
        try{
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setType(type);
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesFromType(categoryDTO));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        try{
            CategoryDTO savedCategory = categoryService.updateCategory(id,categoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body(savedCategory);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
