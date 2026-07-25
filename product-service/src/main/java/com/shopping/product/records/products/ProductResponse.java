package com.shopping.product.records.products;

import com.shopping.product.dtos.productDtos.ProductResponseDto;

import java.util.List;

public record ProductResponse(
        List<ProductResponseDto> products
) {
}
