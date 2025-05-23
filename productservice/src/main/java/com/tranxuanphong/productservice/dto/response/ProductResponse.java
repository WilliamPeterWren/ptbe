package com.tranxuanphong.productservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
  String id;

  String sellerId;

  String productName;

  String categoryId;

  Set<String> productImages;

  String slug;

  String description;

  LocalDate createdAt;

  LocalDate updatedAt;
}
