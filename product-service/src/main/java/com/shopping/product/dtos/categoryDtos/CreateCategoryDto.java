package com.shopping.product.dtos.categoryDtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryDto {

    @NotEmpty(message = "category name can't be empty")
    private String name;
    private String description;

}
