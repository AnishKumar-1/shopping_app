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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductMapper productMapper;
    private final static Logger logger= LoggerFactory.getLogger(ProductService.class);


    //create product under particular category and return category details too
    public ProductResponse createProduct(Long categoryId, List<CreateProductDto> productDto){

        Optional<Category> cat=categoryRepo.findById(categoryId);
         if(cat.isEmpty()){
             logger.error("Category not found with this id: {}",categoryId);
         }
         Category category=cat.get();

       List<Product> products=productMapper.toProducts(productDto);
       logger.info("Successfully converted to product");
       products.forEach(product->product.setCategory(category));

       List<Product> result=productRepo.saveAll(products);
       logger.info("Successfully saved products");
//       CategorySummary categorySummary=CategorySummary.builder().id(category.getId()).name(category.getName()).build();
        List<ProductResponseDto> savedProducts=productMapper.toProductsResponseDto(result);

       return new ProductResponse(savedProducts);
    }

    //get product by passing its id
    public ProductResponseDto getProductByItsId(Long productId){
        Optional<Product> p=productRepo.findById(productId);
        if(p.isEmpty()){
            logger.error("Product not found with this id: {}",productId);
            throw new RuntimeException("Product not found with this id: "+productId);
        }
        Product product=p.get();
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
            logger.error("Product cannot be deleted because product does not exist with this id: {}",productId);
            throw new DataNotFound("Product not found with this id: "+ productId);
        }
        productRepo.deleteById(productId);
        logger.info("Product deleted successfully");
        return "Product deleted successfully.";
    }


    //update product data by its id
    public String updateProduct(Long productId, UpdateProductRepo request){
      Optional<Product> p=productRepo.findById(productId);
      if(p.isEmpty()){
          logger.error("Product not found with this id: {}",productId);
          throw new RuntimeException("Product not found with this id: "+productId);
      }
      Product product=p.get();
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
        logger.info("Product saved successfully");

      return "Product updated successfully.";

    }

    // List of product by category id
    public List<ProductResponseDto> products(Long categoryId){
        if(!categoryRepo.existsById(categoryId)){
            logger.error("Category not found with this id: {}",categoryId);
            throw new IllegalArgumentException("Category not found with this id: "+ categoryId);
        }
        return productRepo.findByCategoryId(categoryId).stream()
                .map(res->productMapper.toProductResponseDto(res)).toList();
    }
}
