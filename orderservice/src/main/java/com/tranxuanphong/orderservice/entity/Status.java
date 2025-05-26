package com.tranxuanphong.orderservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.tranxuanphong.orderservice.enums.OrderStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status {

  @Id
  String id;

  @Builder.Default
  OrderStatus status = OrderStatus.PENDING;

  @Builder.Default
  Instant createdAt = Instant.now();
}
