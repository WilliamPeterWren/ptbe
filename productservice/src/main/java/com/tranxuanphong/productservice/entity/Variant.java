package com.tranxuanphong.productservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Variant {

    @JsonProperty("id")
    String id;

    String variantName;

    Long price;

    Long salePrice;

    Long stock;

    @Builder.Default
    LocalDate createdAt = LocalDate.now();

    @Builder.Default
    LocalDate updatedAt = LocalDate.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Variant other = (Variant) o;

        return Objects.equals(this.id, other.id) &&
               Objects.equals(this.variantName, other.variantName) &&
               Objects.equals(this.price, other.price) &&
               Objects.equals(this.salePrice, other.salePrice) &&
               Objects.equals(this.stock, other.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, variantName, price, stock);
    }

    @Override
    public String toString() {
        return "Variant{" +
               "id='" + id + '\'' +
               ", variantName='" + variantName + '\'' +
               ", price=" + price +
               ", stock=" + stock +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}