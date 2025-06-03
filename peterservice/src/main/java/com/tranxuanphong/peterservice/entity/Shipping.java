package com.tranxuanphong.peterservice.entity;
import java.time.Instant;
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

@Document(collection = "shippings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Shipping {
  @Id
  String id;

  String name;
  Long value;
  String slug;

  @Builder.Default
  boolean available = true;

  @Builder.Default
  Instant createdAt = Instant.now();

  @Builder.Default
  Instant updatedAt = Instant.now();

}
