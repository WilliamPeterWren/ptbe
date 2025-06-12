package com.tranxuanphong.peterservice.dto.model;

import lombok.*;
import lombok.experimental.FieldDefaults;



import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Variant {

    String id;

    String variantName;

    Long price;

    Long salePrice;

    Long stock;

    LocalDate createdAt;

    LocalDate updatedAt;

}