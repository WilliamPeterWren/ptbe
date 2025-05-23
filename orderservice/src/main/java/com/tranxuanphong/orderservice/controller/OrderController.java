package com.tranxuanphong.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.orderservice.dto.request.CreateOrderRequest;
import com.tranxuanphong.orderservice.dto.request.UpdateOrderRequest;
import com.tranxuanphong.orderservice.dto.response.ApiResponse;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
  OrderService orderService;

  @PostMapping
  public ApiResponse<OrderResponse> create(@RequestBody CreateOrderRequest request) {
    return ApiResponse.<OrderResponse>builder()
      .result(orderService.create(request))
      .build();
  }

  // @GetMapping("/get-all")
  // public ApiResponse<List<OrderResponse>> getAll() {
  //   return ApiResponse.<List<OrderResponse>>builder()
  //   .result(orderService.getAll())
  //   .build();
  // }

  // @GetMapping("/get-by-sellerid/{sellerId}")
  // public ApiResponse<List<OrderResponse>> getBySellerID(@PathVariable String sellerId) {
  //   return ApiResponse.<List<OrderResponse>>builder()
  //   .result(orderService.getBySellerID(sellerId))
  //   .build();
  // }

  // @GetMapping("/{slug}")
  // public ApiResponse<OrderResponse> getOne(@PathVariable String slug) {
  //   return ApiResponse.<OrderResponse>builder()
  //   .result(orderService.getOne(slug))
  //   .build();
  // }

  // @PutMapping("/{slug}")
  // public ApiResponse<OrderResponse> upadte(@PathVariable String slug, @RequestBody UpdateOrderRequest request) {
  //   return ApiResponse.<OrderResponse>builder()
  //   .result(orderService.update(slug, request))
  //   .build();
  // }
}
