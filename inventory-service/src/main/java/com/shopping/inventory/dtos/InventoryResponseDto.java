package com.shopping.inventory.dtos;

import com.shopping.inventory.enums.InventoryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDto {

    private Long productId;
    private Integer totalQuantity;
    private InventoryStatus status;
}
