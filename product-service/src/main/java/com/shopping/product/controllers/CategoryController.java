package com.shopping.product.controllers;

import com.shopping.product.dtos.categoryDtos.CategoryResponseDto;
import com.shopping.product.dtos.categoryDtos.CategoryResult;
import com.shopping.product.dtos.categoryDtos.CategoryUpdateDto;
import com.shopping.product.dtos.categoryDtos.CreateCategoryDto;
import com.shopping.product.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryDto dto,@RequestHeader("X-User-Email") String email){
        System.out.println("Request header data: "+ email);
      return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
    }
    //get category by its id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> categoryById(@PathVariable @Min(1) Long id ){
        return ResponseEntity.ok(categoryService.categoryById(id));
    }

    //list all category data
    @GetMapping
    public ResponseEntity<CategoryResult> allCategories(){
        CategoryResult result=new CategoryResult();
        result.setCategories(categoryService.categories());
        return ResponseEntity.ok(result);
    }

    //delete category by its id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCategory(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(categoryService.removeCategory(id));
    }

    //update category name and description
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable @Min(1) Long id, @RequestBody CategoryUpdateDto categoryUpdateDto){
     return ResponseEntity.status(HttpStatus.OK).body(categoryService.changeCategoryNameAndDescription(
                id,categoryUpdateDto
        ));
    }
}
