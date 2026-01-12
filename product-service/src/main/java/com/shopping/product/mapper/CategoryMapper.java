package com.shopping.product.mapper;
import com.shopping.product.dtos.categoryDtos.CategoryResponseDto;
import com.shopping.product.dtos.categoryDtos.CreateCategoryDto;
import com.shopping.product.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toDto(Category entity);
    Category toEntity(CreateCategoryDto dto);

}
