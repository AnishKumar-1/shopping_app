package com.shopping.product.services;

import com.shopping.product.dtos.categoryDtos.CategoryResponseDto;
import com.shopping.product.dtos.categoryDtos.CategoryUpdateDto;
import com.shopping.product.dtos.categoryDtos.CreateCategoryDto;
import com.shopping.product.exceptions.DataNotFound;
import com.shopping.product.exceptions.DuplicateResourceException;
import com.shopping.product.mapper.CategoryMapper;
import com.shopping.product.models.Category;
import com.shopping.product.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryRepo categoryRepo;

    public CategoryResponseDto createCategory(CreateCategoryDto categoryDto) {
        if (categoryRepo.existsByName(categoryDto.getName())) {
            throw new DuplicateResourceException("Category name already exists: "+ categoryDto.getName());
        }
            Category category = categoryMapper.toEntity(categoryDto);
            Category savedCategory = categoryRepo.save(category);
            return categoryMapper.toDto(savedCategory);
    }

    //Category by its id
    public CategoryResponseDto categoryById(Long id) {
        Optional<Category> category = categoryRepo.findById(id);
            if (category.isEmpty()) {
                throw new DataNotFound("Category not found with this id: "+ id);
        }
        return categoryMapper.toDto(category.get());
    }

    //list all category data
    public List<CategoryResponseDto> categories(){
        return categoryRepo.findAll().stream().map(category->categoryMapper.toDto(category)).toList();
    }


    //update category name and description
    public String changeCategoryNameAndDescription(
            Long categoryId, CategoryUpdateDto categoryDto) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new DataNotFound("Category not found with id: " + categoryId));

        // update name if present
        if (categoryDto.getName() != null && !categoryDto.getName().trim().isEmpty()) {

            if (categoryRepo.existsByName(categoryDto.getName())) {
                throw new DuplicateResourceException("Category name already exists");
            }

            category.setName(categoryDto.getName());
        }

        // update description if present
        if (categoryDto.getDescription() != null &&
                !categoryDto.getDescription().trim().isEmpty()) {

            category.setDescription(categoryDto.getDescription());
        }
        categoryRepo.save(category);
        return "Category updated successfully";
    }


    //delete category by its id
    public String removeCategory(Long categoryId){
        Optional<Category> category = categoryRepo.findById(categoryId);
        if (category.isEmpty()) {
            throw new DataNotFound("Category not found with this id: "+ categoryId);
        }
        categoryRepo.deleteById(categoryId);
        return "Category deleted successfully";
    }

}
