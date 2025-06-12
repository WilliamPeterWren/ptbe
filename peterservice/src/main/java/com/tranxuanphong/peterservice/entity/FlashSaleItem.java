package com.tranxuanphong.peterservice.entity;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FlashSaleItem {
  String productId;
  Long price;
  
  String sellerId;
  
  @Builder.Default
  Instant createdAt = Instant.now();

  @Builder.Default
  Instant updatedAt = Instant.now();
}
