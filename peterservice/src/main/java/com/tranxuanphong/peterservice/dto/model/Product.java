package com.tranxuanphong.peterservice.dto.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;


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
public class Product {
    @Id
    String id;

    String sellerId;

    String productName;

    String categoryId;

    String peterCategory;

    @Builder.Default
    Set<String> productImages = new HashSet<>();

    @Builder.Default
    Set<Variant> variants = new HashSet<>();

    @Builder.Default
    Set<Info> infos = new HashSet<>();

    @Indexed(unique = true)
    String slug;

    String description;

    @Builder.Default
    boolean isActive = true;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    Instant updatedAt = Instant.now();
}