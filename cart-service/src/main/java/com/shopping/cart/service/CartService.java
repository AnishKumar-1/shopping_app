package com.shopping.cart.service;


import com.shopping.cart.dto.CartItemRequest;
import com.shopping.cart.dto.CartResponse;

public interface CartService {

    //================ add item to cart ==================================
     CartResponse add_to_cart(CartItemRequest request);

   // ================== update cart quantity =======================
   //update cart
   CartResponse updateQuantity(Long productId, Integer quantity);
  // ========================remove single item from cart ================
    String removeItem(Long productId);
  // ========================= get cart ==================================

    // =========================clear cart ================================
     void clear_cart();

  // =============== get cart ===============================
  CartResponse get_cart();


}
