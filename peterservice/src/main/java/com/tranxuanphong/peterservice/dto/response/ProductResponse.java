package com.tranxuanphong.peterservice.dto.response;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import com.tranxuanphong.peterservice.dto.model.Info;
import com.tranxuanphong.peterservice.dto.model.Variant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
  String id;

  String sellerId;

  String productName;

  String categoryId;

  String peterCategory;

  Set<String> productImages;

  Set<Variant> variants;

  Set<Info> infos;

  String slug;

  Long discount;

  String description;

  Map<Integer, Long> rating;

  Long views;

  boolean isActive;
  
  Long sold;

  String shippingId;

  Instant createdAt;

  Instant updatedAt;
}
