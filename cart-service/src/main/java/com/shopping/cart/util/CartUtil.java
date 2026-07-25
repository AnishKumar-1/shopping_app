package com.shopping.cart.util;

import com.shopping.cart.dto.CartItemsDto;
import com.shopping.cart.model.CartItem;

public class CartUtil {

    private CartUtil(){};
    //cart item to cartITem dto
    public static CartItemsDto cartToCartITemDto(CartItem result){
        return  CartItemsDto.builder().productId(result.getProductId()).productName(result.getProductName())
                .quantity(result.getQuantity()).price(result.getPrice()).imageUrl(result.getImageUrl()).build();
    }
}
