package com.tranxuanphong.productservice.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Variant;

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

  String description;

  Map<Integer, Long> rating;

  Long views;

  boolean isActive;
  
  Long sold;

  String shippingId;

  Instant createdAt;

  Instant updatedAt;
}
