package com.shopping.product.mapper;

import com.shopping.product.dtos.productDtos.CreateProductDto;
import com.shopping.product.dtos.productDtos.ProductResponseDto;
import com.shopping.product.models.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductResponseDto> toProductResponseDto(List<Product> product);
    List<Product> toProduct(List<CreateProductDto> createProductDto);
    List<ProductResponseDto> toProductsResponseDto(List<Product> product);
    List<Product> toProducts(List<CreateProductDto> createProductDto);
    ProductResponseDto toProductResponseDto(Product product);
    Product toProduct(CreateProductDto createProductDto);
}
