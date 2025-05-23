package com.tranxuanphong.productservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "variants")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Variant {
    @Id
    String id;

    String productId;

    String variantName;

    Long price;

    Long stock;

    @Builder.Default
    LocalDate createdAt = LocalDate.now();

    @Builder.Default
    LocalDate updatedAt = LocalDate.now();
}
