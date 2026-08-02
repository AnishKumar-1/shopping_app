package com.shopping.cart.controller;


import com.shopping.cart.dto.CartItemRequest;
import com.shopping.cart.dto.CartResponse;
import com.shopping.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ================= ADD TO CART =================
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody CartItemRequest request,@RequestHeader("X-User-Email") String email) {
        return cartService.add_to_cart(request);
    }

    // ================= UPDATE QUANTITY =================
    @PatchMapping("/{productId}")
    public CartResponse updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return cartService.updateQuantity(productId, quantity);
    }

    // ================= DELETE ITEM =================
    @DeleteMapping("/{productId}")
    public String removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }


   // ============================Clear Cart ===========================

    @DeleteMapping
    public ResponseEntity<Void>clear_cart(){
        cartService.clear_cart();
      return ResponseEntity.noContent().build();
    }
    // ======================= get cart =====================================
    @GetMapping
    public ResponseEntity<CartResponse> get_cart(){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.get_cart());
    }
}
