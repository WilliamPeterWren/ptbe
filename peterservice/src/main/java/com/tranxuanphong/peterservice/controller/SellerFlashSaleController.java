package com.tranxuanphong.peterservice.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranxuanphong.peterservice.dto.request.FlashSaleSellerUpdateRequest;
import com.tranxuanphong.peterservice.dto.request.SellerRemoveProductFromFlashsale;
import com.tranxuanphong.peterservice.dto.response.ApiResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.dto.response.ProductResponse;
import com.tranxuanphong.peterservice.service.SellerFlashSaleService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/flashsales/seller")
public class SellerFlashSaleController {
  SellerFlashSaleService sellerFlashSaleService;

  @PutMapping("/update/seller/{id}")
  public ApiResponse<FlashSaleResponse> updateBySeller(@PathVariable String id, @RequestBody FlashSaleSellerUpdateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(sellerFlashSaleService.updateBySeller(id, request))
    .build();
  } 

  @GetMapping("/get/product/flashsale/id/{id}")
  public Page<ProductResponse> sellerGetProductByFlashsaleId(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return sellerFlashSaleService.sellerGetProductByFlashsaleId(id, page, size);
  }

  @PostMapping("/remove/product/flashsale")
  public void sellerRemoveProductFromFlashsale(@RequestBody SellerRemoveProductFromFlashsale request) {
    sellerFlashSaleService.sellerRemoveProductFromFlashsale(request);
  }
  
}
