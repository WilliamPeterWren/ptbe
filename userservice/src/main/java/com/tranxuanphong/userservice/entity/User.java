package com.tranxuanphong.userservice.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
  @Id
  String id;

  @Indexed(unique = true)
  String email;

  String username;

  String password;

  @Builder.Default
  Set<String> follower = new HashSet<>();
  
  @Builder.Default
  Set<String> following = new HashSet<>();
  
  @Builder.Default
  Map<Integer, Long> rating = new HashMap<>();

  @Builder.Default
  Map<String, Integer> peterVoucher = new HashMap<>();

  @Builder.Default
  Map<String, Integer> shippingVoucher = new HashMap<>();
  
  String avatar;

  @Builder.Default
  String addressId = null;

  @Builder.Default
  Set<Role> roles = new HashSet<>();

  @Builder.Default
  Boolean isActive = true;

  @Builder.Default
  Boolean isVerify = true;

  @Builder.Default
  LocalDate createdAt = LocalDate.now();

  @Builder.Default
  LocalDate updatedAt = LocalDate.now();
}
