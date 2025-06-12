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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

  @GetMapping("/get/items/pageable/{id}")
  public ApiResponse<Page<FlashSaleItemsResponse>> flashSaleItemsResponsePageable(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<FlashSaleItemsResponse>>builder()
    .result(flashSaleService.flashSaleItemsResponsePage(id, page, size))
    .build();
  }

  @GetMapping("/get/discount/flashsale/id/{flashsaleid}/product/id/{productid}")
  public Long getDiscountByFlashSaleIdAndProductId(@PathVariable String flashsaleid, @PathVariable String productid) {
    return flashSaleService.getDiscountByFlashSaleIdAndProductId(flashsaleid, productid);
  }
  
  @GetMapping("/get/flashsale/id/latest")
  public String getLastestFlashSalesId() {
    return flashSaleService.getLastestFlashSalesId();
  }
   
}
