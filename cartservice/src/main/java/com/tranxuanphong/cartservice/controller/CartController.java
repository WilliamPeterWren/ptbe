package com.tranxuanphong.cartservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.cartservice.dto.model.SellerResponse;
import com.tranxuanphong.cartservice.dto.request.AddToCartRequest;
import com.tranxuanphong.cartservice.dto.request.CartUpdateRequest;
import com.tranxuanphong.cartservice.dto.response.ApiResponse;
import com.tranxuanphong.cartservice.dto.response.CartResponse;
import com.tranxuanphong.cartservice.service.CartService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/carts")
// @CrossOrigin(origins = "*")
public class CartController {
  CartService cartService;


  @PostMapping
  public ApiResponse<CartResponse> create() {
    return ApiResponse.<CartResponse>builder()
      .result(cartService.create())
      .build();
  }

  @GetMapping
  public ApiResponse<Set<SellerResponse>> getCart() {
    return ApiResponse.<Set<SellerResponse>>builder()
    .result(cartService.getCart())
    .build();
  }


  @PostMapping("/addtocartt")
  public ApiResponse<Set<SellerResponse>> addToCart(@RequestBody AddToCartRequest request) {
    return ApiResponse.<Set<SellerResponse>>builder()
      .result(cartService.addToCart(request))
      .build();
  }

  @PutMapping
  public ApiResponse<CartResponse> update(@RequestBody CartUpdateRequest request) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.update(request))
    .build();
  }

  @DeleteMapping("/cartitem/id/{variantId}")
  public ApiResponse<CartResponse> deleteItem(@PathVariable String variantId) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.deleteItem(variantId))
    .build();
  }

  @DeleteMapping("/seller/id/{sellerId}")
  public ApiResponse<CartResponse> deleteSeller(@PathVariable String sellerId) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.deleteSeller(sellerId))
    .build();
  }

  @DeleteMapping("/cartitem/variantid/{variantId}")
  public ApiResponse<Set<SellerResponse>> deleteItemResponse(@PathVariable String variantId) {
    // System.out.println("dellllll");
    return ApiResponse.<Set<SellerResponse>>builder()
    .result(cartService.deletee(variantId))
    .build();
  }

  @DeleteMapping("/seller/sellerId/{sellerId}")
  public ApiResponse<Set<SellerResponse>> deleteSellerById(@PathVariable String sellerId) {
    return ApiResponse.<Set<SellerResponse>>builder()
    .result(cartService.deleteSellerById(sellerId))
    .build();
  }

}
