package com.tranxuanphong.peterservice.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleSellerUpdateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.ApiResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleItemsResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.service.FlashSaleService;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/flashsales")
public class FlashSaleController {
  FlashSaleService flashSaleService;

  @PostMapping
  public ApiResponse<FlashSaleResponse> create(@RequestBody FlashSaleCreateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(flashSaleService.create(request))
    .build();
  }

  // @GetMapping
  // public ApiResponse<List<FlashSaleResponse>> getAll() {
  //   return ApiResponse.<List<FlashSaleResponse>>builder()
  //   .result(flashSaleService.getAll())
  //   .build();
  // } 

  @GetMapping("/get/flashsales")
  public ApiResponse<Page<FlashSaleResponse>> getFlashSales(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<FlashSaleResponse>>builder()
    .result(flashSaleService.getPaginatedFlashSales(page, size))
    .build();
  }

  @GetMapping("/get/flashsales/available")
  public ApiResponse<List<FlashSaleResponse>> getValidFlashSales() {
    return ApiResponse.<List<FlashSaleResponse>>builder()
    .result(flashSaleService.getValidFlashSales())
    .build();
  }

  @GetMapping("/{id}")
  public ApiResponse<FlashSaleResponse> getOne(@PathVariable String id) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(flashSaleService.getOne(id))
    .build();
  } 

  @PutMapping("/update/staff/{id}")
  public ApiResponse<FlashSaleResponse> updateByStaff(@PathVariable String id, @RequestBody FlashSaleUpdateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(flashSaleService.updateByStaff(id, request))
    .build();
  } 

  @PutMapping("/update/seller/{id}")
  public ApiResponse<FlashSaleResponse> updateBySeller(@PathVariable String id, @RequestBody FlashSaleSellerUpdateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(flashSaleService.updateBySeller(id, request))
    .build();
  } 
  
  @DeleteMapping("/id")
  public ApiResponse<String> delete(@PathVariable String id){
    return ApiResponse.<String>builder()
    .result(flashSaleService.delete(id))
    .build();
  }

  @GetMapping("/get/items/{id}")
  public ApiResponse<List<FlashSaleItemsResponse>> flashSaleItemsResponse(@PathVariable String id) {
    return ApiResponse.<List<FlashSaleItemsResponse>>builder()
    .result(flashSaleService.flashSaleItemsResponse(id))
    .build();
  }
  
  @GetMapping("/get/items/page/{id}")
  public ApiResponse<List<FlashSaleItemsResponse>> flashSaleItemsResponsePage(@PathVariable String id) {
    return ApiResponse.<List<FlashSaleItemsResponse>>builder()
    .result(flashSaleService.flashSaleItemsResponsePage(id))
    .build();
  }

   
}
