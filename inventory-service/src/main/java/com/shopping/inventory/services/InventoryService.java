package com.shopping.inventory.services;

import com.shopping.inventory.dtos.InventoryRequestDto;
import com.shopping.inventory.dtos.InventoryResponseDto;
import com.shopping.inventory.dtos.ReserverOrReleaseDto;
import com.shopping.inventory.enums.InventoryStatus;
import com.shopping.inventory.exceptions.DuplicateResourceException;
import com.shopping.inventory.exceptions.InsufficientStockException;
import com.shopping.inventory.exceptions.ResourceNotFound;
import com.shopping.inventory.models.Inventory;
import com.shopping.inventory.repositories.InventoryRepo;
import com.shopping.inventory.util.InventoryHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    //create inventory
    public InventoryResponseDto createInventory(Long product_id,InventoryRequestDto inventoryRequestDto){
           if((inventoryRepo.existsByProductId(product_id))){
               throw new DuplicateResourceException("Inventory already exists");
           }

          Inventory inventory= Inventory.builder()
                .totalQuantity(inventoryRequestDto.getTotalQuantity())
                .productId(product_id).build();

        InventoryHelper.recalculateStock(inventory);

           Inventory saved=inventoryRepo.save(inventory);

        return InventoryResponseDto.builder()
                .productId(saved.getProductId())
                .totalQuantity(saved.getTotalQuantity())
                .status(InventoryStatus.IN_STOCK).build();
    }

    // get inventory by product id
    public InventoryResponseDto inventory_by_product_id(Long product_id){
        return inventoryRepo.findByProductId(product_id)
                .map(inv -> InventoryResponseDto.builder()
                        .productId(inv.getProductId())
                        .totalQuantity(inv.getTotalQuantity())
                        .status(inv.getStatus())
                        .build())
                .orElse(
                        InventoryResponseDto.builder()
                                .productId(product_id)
                                .totalQuantity(0)
                                .status(InventoryStatus.OUT_OF_STOCK)
                                .build()
                );
    }

    //Update stock now for user later for admin only
    @Transactional
    public InventoryResponseDto add_stock(Long product_id, Integer quantity){
        Inventory inventory = inventoryRepo.findByProductId(product_id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Inventory not found for productId: " + product_id
                ));
        inventory.setTotalQuantity(inventory.getTotalQuantity() + quantity);
        InventoryHelper.recalculateStock(inventory);
       Inventory updated_inventory=inventoryRepo.save(inventory);
        return InventoryResponseDto.builder()
                .productId(updated_inventory.getProductId())
                .totalQuantity(updated_inventory.getTotalQuantity())
                .status(updated_inventory.getStatus())
                .build();

    }

    //reserve quantity value when payment is processing
    @Transactional
    public ReserverOrReleaseDto reserve_quantity(Long product_id, Integer quantity){
        Inventory inventory = inventoryRepo.findByProductId(product_id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Inventory not found for productId: " + product_id));

        int available = inventory.getTotalQuantity() - inventory.getReservedQuantity();

        if (available < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for productId: " + product_id);
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        InventoryHelper.recalculateStock(inventory);
        Inventory updated_inventory=inventoryRepo.save(inventory);

        return ReserverOrReleaseDto.builder().productId(updated_inventory.getProductId())
                .availableQuantity(updated_inventory.getAvailableQuantity())
                .reservedQuantity(updated_inventory.getReservedQuantity())
                .status(updated_inventory.getStatus()).build();
    }

    //release quantity means order has canceled add again to available quantity and remove this value from reserved quantity
    @Transactional
    public ReserverOrReleaseDto release_quantity(Long product_id, Integer quantity){
        Inventory inventory = inventoryRepo.findByProductId(product_id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Inventory not found for productId: " + product_id
                ));
        if(inventory.getReservedQuantity()<quantity){
            throw new IllegalStateException("Cannot release more than reserved quantity");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventory.setStatus(InventoryStatus.IN_STOCK);
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory updated_inventory=inventoryRepo.save(inventory);

        return ReserverOrReleaseDto.builder().productId(updated_inventory.getProductId())
                .availableQuantity(updated_inventory.getAvailableQuantity()).reservedQuantity(updated_inventory.getReservedQuantity())
                .status(updated_inventory.getStatus()).build();

    }

    // check product in stock or out of stock

    public InventoryStatus check_product_status(Long product_id,int requiredQty){
        Inventory inventory=inventoryRepo.findByProductId(product_id)
                .orElseThrow(()-> new ResourceNotFound("Product not found in inventory"));

        if(inventory.getAvailableQuantity()<requiredQty){
            return InventoryStatus.OUT_OF_STOCK;
        }
        return InventoryStatus.IN_STOCK;
    }


}
