package com.shopping.cart.service.Imp;

import com.shopping.cart.dto.CartItemRequest;
import com.shopping.cart.dto.CartItemsDto;
import com.shopping.cart.dto.CartResponse;
import com.shopping.cart.dto.ProductResponse;
import com.shopping.cart.exception.ServiceUnavailableException;
import com.shopping.cart.feignClient.ProductClient;
import com.shopping.cart.model.CartItem;
import com.shopping.cart.repository.CartRepo;
import com.shopping.cart.service.CartService;
import com.shopping.cart.util.CartUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartImplementation implements CartService {

    private final ProductClient productClient;
    private final CartRepo cartRepo;

    @CircuitBreaker(name="productService", fallbackMethod = "productFallback")
    public CartResponse add_to_cart(CartItemRequest request){
        System.out.println("inside add to cart method"+ request.getProductId());
        ProductResponse product= productClient.getProduct(request.getProductId());
        System.out.println("product details:  "+ product);
        CartItem existing = cartRepo
                .findByProductId(request.getProductId())
                .orElse(null);
        if(existing !=null){
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            cartRepo.save(existing);
        }else{
            CartItem cartItem=CartItem.builder().productId(request.getProductId())
                    .productName(product.getName())
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl()).build();
            cartRepo.save(cartItem);
        }
        List<CartItemsDto> items=cartRepo.findAll().stream().map(cartData->
                CartItemsDto.builder()
                        .productId(cartData.getProductId()).productName(cartData.getProductName())
                        .quantity(cartData.getQuantity()).price(cartData.getPrice())
                        .imageUrl(cartData.getImageUrl()).build()
        ).toList();

        return CartResponse.builder()
                .cart_items(items)
                .build();
    }

    //update cart
    public CartResponse updateQuantity(Long productId, Integer quantity) {
        CartItem item = cartRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(quantity);
        CartItem result= cartRepo.save(item);
        return CartResponse.builder().cart_items(Collections.singletonList(CartUtil.cartToCartITemDto(result))).build();   // return updated cart
    }

    //delete cart item
    public String removeItem(Long productId) {
        CartItem item = cartRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        cartRepo.delete(item);
        return "Product deleted from cart";   // return updated cart
    }

    //list all cart data of a user
    public void clear_cart(){
        cartRepo.deleteAll();
    }


    // get cart
    public CartResponse get_cart(){
        List<CartItemsDto> cartItemsDtos=cartRepo.findAll()
                .stream().map(items->
                        CartItemsDto.builder().productId(items.getProductId())
                                .productName(items.getProductName()).quantity(items.getQuantity())
                                .imageUrl(items.getImageUrl()).price(items.getPrice()).build()
                        ).toList();
        return CartResponse.builder().cart_items(cartItemsDtos).build();
    }



    // fallback must be in same class
    //add to cart fallback method
    public CartResponse productFallback(
            CartItemRequest request,
            Exception ex) {
        throw new ServiceUnavailableException("Product service is currently unavailable.");
    }
}
