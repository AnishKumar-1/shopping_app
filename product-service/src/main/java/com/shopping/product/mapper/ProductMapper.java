package com.shopping.product.mapper;

import com.shopping.product.dtos.productDtos.CreateProductDto;
import com.shopping.product.dtos.productDtos.ProductResponseDto;
import com.shopping.product.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toProductResponseDto(Product product);
    Product toProduct(CreateProductDto createProductDto);
}
