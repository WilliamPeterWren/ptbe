package com.tranxuanphong.orderservice.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tranxuanphong.orderservice.enums.PaymentType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Document(collection = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
  @Id
  String id;

  String userId;
  
  String sellerId;

  @Builder.Default
  Set<OrderItem> orderItems = new HashSet<>();

  @Builder.Default
  Set<Status> orderStatus = new HashSet<>();
  
  String shippingId; // gia cuoc van chuyen -> tao moi ben peterservice, shippingPrice entity

  String shippingVoucherId;

  String sellerVoucherId;

  String addressId;

  PaymentType paymentType;

  @Builder.Default
  Instant createdAt = Instant.now();

  @Builder.Default
  Instant updatedAt = Instant.now();
}
