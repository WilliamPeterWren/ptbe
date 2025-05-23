package com.tranxuanphong.orderservice.entity;

import java.time.LocalDate;
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

  @Builder.Default
  Set<OrderItem> orderItems = new HashSet<>();

  @Builder.Default
  Set<Status> orderStatus = new HashSet<>();
  
  Long shippingVoucherId;

  String addressId;

  PaymentType paymentType;

  @Builder.Default
  LocalDate createdAt = LocalDate.now();

  @Builder.Default
  LocalDate updatedAt = LocalDate.now();
}
