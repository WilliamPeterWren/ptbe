package com.tranxuanphong.productservice.dto.request;

import java.util.Map;

import lombok.Getter;

@Getter
public class ProductUpdateRatingRequest {
  Map<Integer, Long> rating;
}
