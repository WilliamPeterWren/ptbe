package com.tranxuanphong.orderservice.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import com.tranxuanphong.orderservice.model.Info;
import com.tranxuanphong.orderservice.model.Variant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

  String productId;
  String productName;
  String variantId;
  String variantName;
  String slug;
  String image;
  Long price;
  Long salePrice;

}
