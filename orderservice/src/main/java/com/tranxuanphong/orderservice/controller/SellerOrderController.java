package com.tranxuanphong.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.orderservice.dto.request.OrderUpdateRequest;
import com.tranxuanphong.orderservice.dto.response.ApiResponse;
import com.tranxuanphong.orderservice.dto.response.OrderResponseFE;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.service.SellerOrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/orders/seller")
public class SellerOrderController {
  SellerOrderService sellerOrderService;
  @PutMapping("/id/{orderId}")
  public ApiResponse<OrderResponseFE> update(@PathVariable String orderId, @RequestBody OrderUpdateRequest request) {
    return ApiResponse.<OrderResponseFE>builder()
      .result(sellerOrderService.update(orderId, request))
      .build();
  }

  @GetMapping("/getall")
  public Page<OrderResponseFE> getAllOrderBySeller(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return sellerOrderService.getAllOrderBySeller(page, size);
  }

  @GetMapping("/orderstatus/{orderstatus}")
  public Page<OrderResponseFE> getOrderByStatus(@PathVariable OrderStatus orderstatus, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return sellerOrderService.getOrderByStatus(orderstatus, page, size);
  }

  @GetMapping("/id/{id}")
  public OrderResponseFE getOrderById(@PathVariable String id) {
    return sellerOrderService.getOrderById(id);
  }

}
