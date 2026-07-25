package com.shopping.product.controllers;

import com.shopping.product.dtos.productDtos.CreateProductDto;
import com.shopping.product.dtos.productDtos.ProductResponseDto;
import com.shopping.product.dtos.productDtos.UpdateProductRepo;
import com.shopping.product.records.products.ProductResponse;
import com.shopping.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    //create product
    @PostMapping("/products/{categoryid}")
    public ResponseEntity<ProductResponse> createProduct(@PathVariable Long categoryid, @RequestBody List<CreateProductDto> productDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(categoryid,productDto));
    }

    //get product by its id
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDto> product(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductByItsId(productId));
    }

    //list of products
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> products(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.products());
    }

    //delete product by its id
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.removeProduct(productId));
    }

    //update product by its id and take data from user in json form
    @PutMapping("/product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRepo request){
        return ResponseEntity.ok(productService.updateProduct(productId,request));
    }

    //List of products by category id
    @GetMapping("/products/{categoryId}/products")
    public ResponseEntity<List<ProductResponseDto>> productsByCategoryId(@PathVariable Long categoryId){
        return ResponseEntity.ok(productService.products(categoryId));
    }

}
