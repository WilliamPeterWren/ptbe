package com.tranxuanphong.orderservice.dto.request;

import com.tranxuanphong.orderservice.enums.OrderStatus;

import lombok.Getter;

@Getter
public class OrderUpdateRequest {
  OrderStatus orderStatus;
  String recieveImage;
}
