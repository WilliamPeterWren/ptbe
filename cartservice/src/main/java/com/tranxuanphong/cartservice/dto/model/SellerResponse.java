package com.tranxuanphong.cartservice.dto.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SellerResponse {
  String sellerId;
  String sellerUsername;

  @Builder.Default
  Set<ItemResponse> itemResponses = new HashSet<>();

  @Builder.Default
  Instant updatedAt = Instant.now();
}
