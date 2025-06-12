package com.tranxuanphong.productservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Document(collection = "reviews")
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data 
public class Review {

    @Id
    String id;

    String userId;
    String productId;
    String variantId;
    String orderId;
    String comment;

    @Builder.Default    
    String image = null;
    int star;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    Instant updatedAt = Instant.now();
}