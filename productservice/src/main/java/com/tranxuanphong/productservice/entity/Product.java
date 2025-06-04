package com.tranxuanphong.productservice.entity;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;
// import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "products")
// @org.springframework.data.elasticsearch.annotations.Document(indexName = "products")
// @org.springframework.data.elasticsearch.annotations.Document(
//     indexName = "products", // The Elasticsearch index name for this entity
//     createIndex = true, // Automatically create the index if it doesn't exist
//     writeTypeHint = WriteTypeHint.FALSE // Avoids adding _class field to ES document
// )
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
    String shippingId = "683b529e2a9cfc41ae6f134b"; 

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
    Map<Integer, Long> rating = new HashMap<>();

    @Builder.Default
    Long sold = 0L;

    @Builder.Default
    Long views = 0L;

    @Builder.Default
    boolean isActive = true;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    Instant updatedAt = Instant.now();
}