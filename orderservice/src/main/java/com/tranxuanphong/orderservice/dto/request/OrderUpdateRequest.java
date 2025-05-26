package com.tranxuanphong.orderservice.dto.request;

import com.tranxuanphong.orderservice.entity.Status;

import lombok.Getter;

@Getter
public class OrderUpdateRequest {
  Status orderStatus;
}
