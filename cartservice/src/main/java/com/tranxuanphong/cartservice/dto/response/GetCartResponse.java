package com.tranxuanphong.cartservice.dto.response;

import java.util.List;

import com.tranxuanphong.cartservice.entity.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class GetCartResponse {
  String cartId; 
  String userId;
  List<CartItem> cartItems;
}
