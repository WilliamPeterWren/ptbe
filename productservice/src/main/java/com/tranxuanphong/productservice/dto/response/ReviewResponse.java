package com.tranxuanphong.productservice.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
  String id;

  String username;
  String avatar;
  String productId;
  String variantId;
  String comment;

  String image;
  int star;

  Instant createdAt;

  Instant updatedAt;
}
