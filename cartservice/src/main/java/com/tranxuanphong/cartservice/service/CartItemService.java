package com.tranxuanphong.cartservice.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tranxuanphong.cartservice.dto.request.AddToCartRequest;
import com.tranxuanphong.cartservice.dto.request.UpdateCartRequest;
import com.tranxuanphong.cartservice.dto.response.GetCartResponse;
import com.tranxuanphong.cartservice.entity.CartItem;
import com.tranxuanphong.cartservice.entity.Cart;
import com.tranxuanphong.cartservice.exception.AppException;
import com.tranxuanphong.cartservice.exception.ErrorCode;
import com.tranxuanphong.cartservice.repository.CartItemRepository;
import com.tranxuanphong.cartservice.repository.CartRepository;
import com.tranxuanphong.cartservice.repository.httpclient.UserClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class CartItemService {
  CartItemRepository cartItemRepository;
  CartRepository cartRepository;
  UserClient userClient;

  @PreAuthorize("hasRole('ROLE_USER')")
  public GetCartResponse addToCart(AddToCartRequest request){
    Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    
    if(!userClient.existsId(request.getUserId()) || !cart.getUserId().equals(request.getUserId()) ){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    List<CartItem> list = cartItemRepository.findByCartId(request.getCartId());

    for(CartItem c:list){
      if(c.getVariantId().equals(request.getVariantId())){
        c.setQuantity(c.getQuantity() + request.getQuantity());

        System.out.println("variants duplicate!!!");

        return GetCartResponse.builder()
          .cartId(request.getCartId())
          .userId(request.getUserId())
          .cartItems(list)
          .build();
      }
    }


    CartItem cartItem = CartItem.builder()
    .cartId(request.getCartId())
    .variantId(request.getVariantId())
    .quantity(request.getQuantity())
    .build();

    cartItemRepository.save(cartItem);

    return GetCartResponse.builder()
    .cartId(request.getCartId())
    .userId(request.getUserId())
    .cartItems(cartItemRepository.findByCartId(request.getCartId()))
    .build();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public GetCartResponse updateCart(String cardId, UpdateCartRequest request){
    Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    
    if(!userClient.existsId(request.getUserId()) || !cart.getUserId().equals(request.getUserId()) ){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(request.getQuantity() <= 0){
      cartItemRepository.deleteById(request.getCartItemId());

      return GetCartResponse.builder()
        .cartId(cardId)
        .userId(request.getUserId())
        .cartItems(cartItemRepository.findByCartId(cardId))
        .build();
    }

    CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

    cartItem.setQuantity(request.getQuantity());

    cartItemRepository.save(cartItem);

    List<CartItem> list = cartItemRepository.findByCartId(cardId);

    return GetCartResponse.builder()
    .cartId(cardId)
    .userId(request.getUserId())
    .cartItems(list)
    .build();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public void deleteCartItem(String cartItemId){
    cartItemRepository.deleteById(cartItemId);
  }
}
