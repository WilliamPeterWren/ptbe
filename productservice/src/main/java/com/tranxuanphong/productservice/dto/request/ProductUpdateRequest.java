package com.tranxuanphong.productservice.dto.request;

import java.util.Set;

import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Variant;

import lombok.Getter;

@Getter
public class ProductUpdateRequest {
  String productName;

  String categoryId;

  String peterCategory;

  Set<String> productImages;

  String description;

  Set<Variant> variants;

  Set<Info> infos;

  String shippingId;

}
