package com.shopping.product.services;

import com.shopping.product.dtos.categoryDtos.CategorySummary;
import com.shopping.product.dtos.productDtos.CreateProductDto;
import com.shopping.product.dtos.productDtos.ProductResponseDto;
import com.shopping.product.dtos.productDtos.UpdateProductRepo;
import com.shopping.product.enums.Status;
import com.shopping.product.exceptions.DataNotFound;
import com.shopping.product.mapper.ProductMapper;
import com.shopping.product.models.Category;
import com.shopping.product.models.Product;
import com.shopping.product.repositories.CategoryRepo;
import com.shopping.product.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductMapper productMapper;


    //create product under particular category and return category details too
    public ProductResponseDto createProduct(Long categoryId, CreateProductDto productDto){
       Category category=categoryRepo.findById(categoryId).orElseThrow(()->new DataNotFound("Category not found"));
       Product product=productMapper.toProduct(productDto);
       product.setCategory(category);
       product.setStatus(Status.ACTIVE);
       Product result=productRepo.save(product);
       CategorySummary categorySummary=CategorySummary.builder().id(category.getId()).name(category.getName()).build();
        return productMapper.toProductResponseDto(result);
    }

    //get product by passing its id
    public ProductResponseDto getProductByItsId(Long productId){
        Product product=productRepo.findById(productId).orElseThrow(
                ()->new DataNotFound("Product not found with this id: "+ productId));
        return productMapper.toProductResponseDto(product);
    }

    //List of products

    public List<ProductResponseDto>  products(){
        return productRepo.findAll().stream().map(
                   singleProduct-> productMapper.toProductResponseDto(singleProduct)
           ).toList();
    }

    //delete product by its id
    public String removeProduct(Long productId){
        if(!productRepo.existsById(productId)){
            throw new DataNotFound("Product not found with this id: "+ productId);
        }
        productRepo.deleteById(productId);
        return "Product deleted successfully.";
    }


    //update product data by its id
    public String updateProduct(Long productId, UpdateProductRepo request){
      Product product=productRepo.findById(productId).orElseThrow(()->new DataNotFound("Product not found with this id: "+ productId));
      if(request.getName() != null && !request.getName().isEmpty()){
          product.setName(request.getName());
      }
      if(request.getDescription() != null && !request.getDescription().isEmpty()){
          product.setDescription(request.getDescription());
      }
      if(request.getPrice() != null){
          product.setPrice(request.getPrice());
      }
      productRepo.save(product);

      return "Product updated successfully.";

    }

    // List of product by category id
    public List<ProductResponseDto> products(Long categoryId){
        if(!categoryRepo.existsById(categoryId)){
            throw new IllegalArgumentException("Category not found with this id: "+ categoryId);
        }
        return productRepo.findByCategoryId(categoryId).stream()
                .map(res->productMapper.toProductResponseDto(res)).toList();
    }
}
