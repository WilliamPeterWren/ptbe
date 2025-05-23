package com.tranxuanphong.cartservice.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.tranxuanphong.cartservice.dto.request.CreateCartRequest;
import com.tranxuanphong.cartservice.dto.response.CreateCartResponse;
import com.tranxuanphong.cartservice.dto.response.GetCartResponse;
import com.tranxuanphong.cartservice.entity.Cart;
import com.tranxuanphong.cartservice.entity.CartItem;
import com.tranxuanphong.cartservice.exception.AppException;
import com.tranxuanphong.cartservice.exception.ErrorCode;
import com.tranxuanphong.cartservice.repository.CartItemRepository;
import com.tranxuanphong.cartservice.repository.CartRepository;
import com.tranxuanphong.cartservice.repository.httpclient.UserClient;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class CartService {
  CartRepository cartRepository;
  CartItemRepository cartItemRepository;
  UserClient userClient;

  
  // @PreAuthorize("hasRole('ROLE_USER')")
  public CreateCartResponse create(CreateCartRequest request){

    System.out.println("yes 00");
    if(!userClient.existsId(request.getUserId())){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(cartRepository.existsByUserId(request.getUserId())){
      throw new AppException(ErrorCode.CART_EXISTS);
    }

    System.out.println("yes 01");

    Cart cart = Cart.builder()
    .userId(request.getUserId())
    .build();

    cartRepository.save(cart);

    System.out.println("yes 02");
    CreateCartResponse response = CreateCartResponse.builder()
    .cartId(cart.getId())
    .userId(request.getUserId())
    .build();

    System.out.println("yes 03: " + response.toString());

    return response;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public GetCartResponse getCart(String userId){

    if(!userClient.existsId(userId)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    List<CartItem> list = cartItemRepository.findByCartId(cart.getId());
    
    return GetCartResponse.builder()
    .cartId(cart.getId())
    .userId(userId)
    .cartItems(list)
    .build();
  }





}
