package com.shopping.inventory.util;

import com.shopping.inventory.enums.InventoryStatus;
import com.shopping.inventory.models.Inventory;


public final class InventoryHelper {

    private InventoryHelper(){
    }

    public static void recalculateStock(Inventory inventory){
        int availableQuantity=inventory.getTotalQuantity()-inventory.getReservedQuantity();
        inventory.setAvailableQuantity(Math.max(availableQuantity,0));
        inventory.setStatus(inventory.getAvailableQuantity()>0? InventoryStatus.IN_STOCK:InventoryStatus.OUT_OF_STOCK);
    }
}
