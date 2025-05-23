package com.tranxuanphong.productservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;


@Document(collection = "categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Category {
    @Id
    String id;

    String sellerId;

    String categoryName;

    String slug;

    @Builder.Default
    LocalDate createdAt = LocalDate.now();

    @Builder.Default
    LocalDate updatedAt = LocalDate.now();

}
