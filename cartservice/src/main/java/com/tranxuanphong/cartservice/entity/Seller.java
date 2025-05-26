package com.tranxuanphong.cartservice.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seller {
  @Id
  String id;
  String sellerId;
  Set<CartItem> cartItems;
}
