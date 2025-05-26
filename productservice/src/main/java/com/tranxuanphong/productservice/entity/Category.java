package com.tranxuanphong.productservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "categories")
// @org.springframework.data.elasticsearch.annotations.Document(indexName = "categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Category {
    @Id
    String id;

    // @Field(type = FieldType.Text)
    String sellerId;

    // @Field(type = FieldType.Text)
    String categoryName;

    // @Field(type = FieldType.Text)
    String slug;

    @Builder.Default
    Set<String> peterCategories = new HashSet<>();

    @Builder.Default
    // @Field(type = FieldType.Date)
    LocalDate createdAt = LocalDate.now();

    @Builder.Default
    // @Field(type = FieldType.Date)
    LocalDate updatedAt = LocalDate.now();
}