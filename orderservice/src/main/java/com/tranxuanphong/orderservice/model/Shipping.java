package com.tranxuanphong.orderservice.model;
import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Shipping {
  String id;

  String name;
  Long value;
  String slug;

  boolean available;

  Instant createdAt;

  Instant updatedAt;

}
