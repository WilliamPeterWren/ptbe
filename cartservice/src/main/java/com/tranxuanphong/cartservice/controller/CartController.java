package com.tranxuanphong.cartservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.cartservice.dto.request.CreateCartRequest;
import com.tranxuanphong.cartservice.dto.response.ApiResponse;
import com.tranxuanphong.cartservice.dto.response.CreateCartResponse;
import com.tranxuanphong.cartservice.dto.response.GetCartResponse;
import com.tranxuanphong.cartservice.service.CartService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
  CartService cartService;


  @PostMapping
  public ApiResponse<CreateCartResponse> create(@RequestBody CreateCartRequest request) {
    System.out.println("cart con troller");

    CreateCartResponse response = cartService.create(request);
    System.out.println("cart id: " + response.getCartId() + " userid: " + response.getUserId());
    return ApiResponse.<CreateCartResponse>builder()
      .result(response)
      .build();
  }

  @GetMapping("/{userId}")
  public ApiResponse<GetCartResponse> getCart(@PathVariable String userId) {

    // System.out.println("user id: " + userId);

    // GetCartResponse response = cartService.getCart(userId);

    // System.out.println("user id: " + response.getCartId() + "cart id: " + response.getUserId());

    // System.out.println("list: " + response.getCartItems().toString());
    return ApiResponse.<GetCartResponse>builder()
    .result(cartService.getCart(userId))
    .build();
  }

}
