package com.shopping.order.mappers;

import com.shopping.order.dto.orderItemDto.OrderItemsResponseDto;
import com.shopping.order.models.OrderItems;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {

  //OrderItemsDto OrderItems
    OrderItems mapToOrderItems(OrderItemsResponseDto orderItems);
    //map to orderItemDto from OrderITems
    OrderItemsResponseDto mapToOrderItemsDto(OrderItems orderItems);
}
