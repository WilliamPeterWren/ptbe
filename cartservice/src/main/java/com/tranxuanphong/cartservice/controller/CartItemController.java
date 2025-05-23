package com.tranxuanphong.cartservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.cartservice.dto.request.AddToCartRequest;
import com.tranxuanphong.cartservice.dto.request.UpdateCartRequest;
import com.tranxuanphong.cartservice.dto.response.ApiResponse;
import com.tranxuanphong.cartservice.dto.response.GetCartResponse;
import com.tranxuanphong.cartservice.service.CartItemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/cartitems")
public class CartItemController {
  CartItemService cartItemService;


  @PostMapping
  public ApiResponse<GetCartResponse> addToCart(@RequestBody AddToCartRequest request) {
    return ApiResponse.<GetCartResponse>builder()
      .result(cartItemService.addToCart(request))
      .build();
  }

  @PutMapping("/{cartId}")
  public ApiResponse<GetCartResponse> upadte(@PathVariable String cartId, @RequestBody UpdateCartRequest request) {
    return ApiResponse.<GetCartResponse>builder()
    .result(cartItemService.updateCart(cartId, request))
    .build();
  }
  
  @DeleteMapping("/{cartItemId}")
  public ApiResponse<String> delete(@PathVariable String cartItemId){
    cartItemService.deleteCartItem(cartItemId);

    return ApiResponse.<String>builder()
    .result("Success delete cart item")
    .build();
  }
}
