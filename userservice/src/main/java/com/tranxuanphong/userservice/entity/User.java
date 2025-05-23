package com.tranxuanphong.userservice.entity;

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

  String email;
  String password;

  @Builder.Default
  Address address = null;

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
