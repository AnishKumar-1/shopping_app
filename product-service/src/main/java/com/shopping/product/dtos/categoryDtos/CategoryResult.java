package com.shopping.product.dtos.categoryDtos;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResult {

    private List<CategoryResponseDto> categories;
}
