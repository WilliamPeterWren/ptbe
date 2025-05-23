package com.tranxuanphong.productservice.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Product {
  @Id
  String id;

  String sellerId;

  String productName;

  String categoryId;

  @Builder.Default
  Set<String> productImages = new HashSet<>();

  String slug;

  String description;

  @Builder.Default
  LocalDate createdAt = LocalDate.now();

  @Builder.Default
  LocalDate updatedAt = LocalDate.now();
}
