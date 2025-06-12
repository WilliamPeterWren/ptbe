package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.service.ShipperProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products/shipper")
public class ShipperController {
  ShipperProductService shipperProductService;
  @PutMapping("/id/{id}/sold/{sold}")
  public ApiResponse<ProductResponse> updateSoldById(@PathVariable String id, @PathVariable Long sold) {
    return ApiResponse.<ProductResponse>builder()
    .result(shipperProductService.updateSoldById(id, sold))
    .build();
  }
}
