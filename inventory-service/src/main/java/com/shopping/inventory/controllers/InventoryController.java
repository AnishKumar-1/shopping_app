package com.shopping.inventory.controllers;

import com.shopping.inventory.dtos.InventoryCheckRequest;
import com.shopping.inventory.dtos.InventoryRequestDto;
import com.shopping.inventory.dtos.InventoryResponseDto;
import com.shopping.inventory.dtos.ReserverOrReleaseDto;
import com.shopping.inventory.enums.InventoryStatus;
import com.shopping.inventory.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    //method to create inventory taking json values from body
    @PostMapping("/product/{product_id}")
    public ResponseEntity<InventoryResponseDto> create_inventory(@PathVariable  Long product_id , @Valid @RequestBody InventoryRequestDto inventoryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createInventory(product_id,inventoryRequestDto));
    }

    // get inventory by product id
    @GetMapping("/product/{product_id}")
    public ResponseEntity<InventoryResponseDto> inventoryByProductId(@PathVariable Long product_id){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.inventory_by_product_id(product_id));
    }

    //update stock by product id and integer stock value
    @PostMapping("/{product_id}/update")
    public ResponseEntity<InventoryResponseDto> upDateStock(@PathVariable Long product_id,@RequestParam Integer quantity){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.add_stock(product_id,quantity));
    }

    //reserved quantity
    @PostMapping("/{product_id}/reserve")
    public ResponseEntity<ReserverOrReleaseDto> reserve(@PathVariable Long product_id, @RequestParam Integer quantity){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.reserve_quantity(product_id,quantity));
    }

    //release reserved quantity due to order cancel or payment not made
    @PostMapping("/{product_id}/release")
    public ResponseEntity<ReserverOrReleaseDto> releaseQuantity(@PathVariable Long product_id, @RequestParam Integer quantity){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.release_quantity(product_id,quantity));
    }

   //check product status if in stock or not
    @PostMapping("/check")
    public InventoryStatus check_product_availability(@Valid @RequestBody InventoryCheckRequest request){
        return inventoryService.check_product_status(request.getProductId(), request.getQuantity());
    }

    //List all the inventory data
    @GetMapping("/list")
    public ResponseEntity<List<InventoryResponseDto>> list_inventory(){
        return ResponseEntity.ok(inventoryService.list_all_inventory());
    }
}
