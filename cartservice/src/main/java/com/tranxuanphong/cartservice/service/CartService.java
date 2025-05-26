package com.tranxuanphong.cartservice.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.cartservice.dto.request.AddToCartRequest;
import com.tranxuanphong.cartservice.dto.request.CartUpdateRequest;
import com.tranxuanphong.cartservice.dto.response.CartResponse;
import com.tranxuanphong.cartservice.entity.Cart;
import com.tranxuanphong.cartservice.entity.CartItem;
import com.tranxuanphong.cartservice.entity.Seller;
import com.tranxuanphong.cartservice.exception.AppException;
import com.tranxuanphong.cartservice.exception.ErrorCode;
import com.tranxuanphong.cartservice.repository.CartRepository;
import com.tranxuanphong.cartservice.repository.httpclient.ProductClient;
import com.tranxuanphong.cartservice.repository.httpclient.UserClient;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class CartService {
  CartRepository cartRepository;

  UserClient userClient;
  ProductClient productClient;

  
  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse create(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = Cart.builder()
    .userId(userId)
    .build();

    cartRepository.save(cart);

    return CartResponse.builder()
    .build();

  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse getCart(){

    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    return CartResponse.builder()
    .sellers(cart.getSellers())
    .build();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse addToCart(AddToCartRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(request.getVariantId() != null && !productClient.doesVariantExistById(request.getVariantId())){
      throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();

    for(Seller seller: sellers){
      if(seller.getSellerId().equals(request.getSellerId())){
        Set<CartItem> cartItems = seller.getCartItems();

        for(CartItem cartItem: cartItems){
          if(cartItem.getVariantId().equals(request.getVariantId())){
            cartItem.setQuantity(request.getQuantity());         
            seller.setCartItems(cartItems);
            cart.setSellers(sellers);
            return CartResponse.builder()
              .sellers(sellers)
              .build();
          }
        }
   
        CartItem cartItem = CartItem.builder()
        .quantity(request.getQuantity())
        .variantId(request.getVariantId())
        .build();
      
        cartItems.add(cartItem);
        seller.setCartItems(cartItems);       
        
        cart.setSellers(sellers);
        cartRepository.save(cart);
        
        return CartResponse.builder()
        .sellers(sellers)
        .build();
      }
      
    }

    CartItem cartItem = CartItem.builder()
      .quantity(request.getQuantity())
      .variantId(request.getVariantId())
      .build();

    Set<CartItem> cartItems = new HashSet<>();
    cartItems.add(cartItem);

    Seller seller = Seller.builder()
    .sellerId(request.getSellerId())
    .cartItems(cartItems)
    .build();

    sellers.add(seller);
        
    cart.setSellers(sellers);
    cartRepository.save(cart);
    
    return CartResponse.builder()
    .sellers(sellers)
    .build();
  }
  
  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse update(CartUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(request.getVariantId() != null && !productClient.doesVariantExistById(request.getVariantId())){
      throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers){
      Set<CartItem> cartItems = seller.getCartItems();
      for(CartItem cartItem : cartItems){
        if(cartItem.getVariantId().equals(request.getVariantId())){
          cartItem.setQuantity(request.getQuantity());

          seller.setCartItems(cartItems);
          cart.setSellers(sellers);
          cartRepository.save(cart);

          return CartResponse.builder()
          .sellers(sellers)
          .build();
        }
      }
    }

    // CartItem cartItem = CartItem.builder()
    // .variantId(request.getVariantId())
    // .quantity(request.getQuantity())
    // .build();

    // return CartResponse.builder()
    // .sellers(sellers)
    // .build();

    throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse deleteCartItem(String cartItemId){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers){
      Set<CartItem> cartItems = seller.getCartItems();
      for(CartItem cartItem : cartItems){
        if(cartItem.getId().equals(cartItemId)){
          cartItems.remove(cartItem);

          seller.setCartItems(cartItems);
          cart.setSellers(sellers);

          cartRepository.save(cart);

          return CartResponse.builder()
          .sellers(sellers)
          .build();
        }
      }
    }


    throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse deleteSeller(String sellerId){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(!userClient.doesUserExistById(sellerId)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers){
      if(seller.getId().equals(sellerId)){
        sellers.remove(seller);

        cart.setSellers(sellers);

        cartRepository.save(cart);

        return CartResponse.builder()
          .sellers(sellers)
          .build();
      }
    }


    throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

  
}
