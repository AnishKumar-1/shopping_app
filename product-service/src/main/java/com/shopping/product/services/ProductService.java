package com.shopping.product.services;
import com.shopping.product.dtos.productDtos.CreateProductDto;
import com.shopping.product.dtos.productDtos.ProductResponseDto;
import com.shopping.product.dtos.productDtos.UpdateProductRepo;
import com.shopping.product.exceptions.DataNotFound;
import com.shopping.product.mapper.ProductMapper;
import com.shopping.product.models.Category;
import com.shopping.product.models.Product;
import com.shopping.product.records.products.ProductResponse;
import com.shopping.product.repositories.CategoryRepo;
import com.shopping.product.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductMapper productMapper;


    //create product under particular category and return category details too
    public ProductResponse createProduct(Long categoryId, List<CreateProductDto> productDto){

        Category category=categoryRepo.findById(categoryId).orElseThrow(()->new DataNotFound("Category not found"));
       List<Product> products=productMapper.toProducts(productDto);
       products.forEach(product->product.setCategory(category));

       List<Product> result=productRepo.saveAll(products);
//       CategorySummary categorySummary=CategorySummary.builder().id(category.getId()).name(category.getName()).build();
        List<ProductResponseDto> savedProducts=productMapper.toProductsResponseDto(result);
       return new ProductResponse(savedProducts);
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

        if(request.getImageUrl() != null && !request.getImageUrl().isEmpty()){
            product.setImageUrl(request.getImageUrl());
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
