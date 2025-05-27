package com.tranxuanphong.cartservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
  CartService cartService;


  @PostMapping
  public ApiResponse<CartResponse> create() {
    return ApiResponse.<CartResponse>builder()
      .result(cartService.create())
      .build();
  }

  @GetMapping
  public ApiResponse<CartResponse> getCart() {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.getCart())
    .build();
  }

  @PostMapping("/addtocart")
  public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
    return ApiResponse.<CartResponse>builder()
      .result(cartService.addToCart(request))
      .build();
  }

  @PutMapping
  public ApiResponse<CartResponse> update(@RequestBody CartUpdateRequest request) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.update(request))
    .build();
  }

  @DeleteMapping("/caritem/id/{cartItemId}")
  public ApiResponse<CartResponse> deleteCartItem(@PathVariable String cartItemId) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.deleteCartItem(cartItemId))
    .build();
  }

  @DeleteMapping("/seller/id/{sellerId}")
  public ApiResponse<CartResponse> deleteSeller(@PathVariable String sellerId) {
    return ApiResponse.<CartResponse>builder()
    .result(cartService.deleteSeller(sellerId))
    .build();
  }

}
