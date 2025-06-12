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
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.ApiResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.service.AdminFlashSaleService;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/flashsales/admin")
public class AdminFlashSaleController {
  AdminFlashSaleService adminFlashSaleService;

  @GetMapping("update/all")
  public void adminUpdateAll(){
    adminFlashSaleService.adminUpdateAll();
  }

  @PostMapping
  public ApiResponse<FlashSaleResponse> create(@RequestBody FlashSaleCreateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(adminFlashSaleService.create(request))
    .build();
  }

  @PutMapping("/update/staff/{id}")
  public ApiResponse<FlashSaleResponse> updateByStaff(@PathVariable String id, @RequestBody FlashSaleUpdateRequest request) {
    return ApiResponse.<FlashSaleResponse>builder()
    .result(adminFlashSaleService.updateByStaff(id, request))
    .build();
  } 

  @DeleteMapping("/id/{id}")
  public void adminDeleteFlashsale(@PathVariable String id){
    adminFlashSaleService.adminDeleteFlashsale(id);
  }

  @GetMapping("/get/flashsales")
  public List<FlashSaleResponse> getFlashSales() {
    return adminFlashSaleService.getListFlashSales();
  }


}
