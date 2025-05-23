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
@RequestMapping("/api/status")
public class StatusController {
  
}
