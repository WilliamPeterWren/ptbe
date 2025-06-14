package com.tranxuanphong.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.orderservice.dto.request.OrderCreateRequest;
import com.tranxuanphong.orderservice.dto.response.ApiResponse;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.dto.response.OrderResponseFE;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
  OrderService orderService;

  @PostMapping
  public ApiResponse<OrderResponse> create(@RequestBody OrderCreateRequest request) {
    return ApiResponse.<OrderResponse>builder()
      .result(orderService.create(request))
      .build();
  }

  @GetMapping("/exists-by-address/{addressId}")
  public boolean doesAddressExist(@PathVariable String addressId) {
    return orderService.doesAddressExist(addressId);
  }
  
  @GetMapping("/user/getall")
  public Page<OrderResponseFE> getAllOrderByUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return orderService.getAllOrderByUser(page, size);
  }

  @GetMapping("/user/orderstatus/{orderstatus}")
  public Page<OrderResponseFE> getOrderByStatus(@PathVariable OrderStatus orderstatus, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return orderService.getOrderByStatus(orderstatus, page, size);
  }


  


  
}
