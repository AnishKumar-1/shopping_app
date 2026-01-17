package com.shopping.order.dto.feignDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFeignResponseDto{
    private Long id;
    private String name;
    private BigDecimal price;
}
