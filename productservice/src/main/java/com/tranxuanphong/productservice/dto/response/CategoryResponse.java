package com.tranxuanphong.productservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
  String id;
  String sellerId;
  String categoryName;
  String slug;
}
