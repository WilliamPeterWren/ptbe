package com.tranxuanphong.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tranxuanphong.userservice.dto.response.SellerInfoResponse;
import com.tranxuanphong.userservice.service.SellerUserService;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users/seller")
public class SellerController {
  SellerUserService sellerUserService;

  @GetMapping("/get/seller/info/id/{sellerId}")
  public SellerInfoResponse getSellerInfoResponse(@PathVariable String sellerId){
    return sellerUserService.getSellerInfo(sellerId);
  }
}
