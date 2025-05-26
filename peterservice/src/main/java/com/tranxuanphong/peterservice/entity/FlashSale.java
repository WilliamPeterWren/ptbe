package com.tranxuanphong.peterservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "flashsales")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FlashSale {
  @Id
  String id;
  String productId;
  Long price;
  Instant expiredAt;
}
