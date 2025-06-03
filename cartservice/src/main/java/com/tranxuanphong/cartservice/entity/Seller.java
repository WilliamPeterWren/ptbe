package com.tranxuanphong.cartservice.entity;

import java.time.Instant;
import java.util.Set;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seller {

  String sellerId;
  Set<Item> items;

  @Builder.Default
  Instant updatedAt = Instant.now();
}
