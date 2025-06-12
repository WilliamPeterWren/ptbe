package com.tranxuanphong.productservice.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Variant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  Boolean isActive;
  
  Long sold;

  Long discount;

  String shippingId;

  Instant createdAt;

  Instant updatedAt;
}
