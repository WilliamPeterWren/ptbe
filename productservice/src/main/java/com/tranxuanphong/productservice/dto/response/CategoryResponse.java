package com.tranxuanphong.productservice.dto.response;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
  String id;
  String sellerId;
  String categoryName;
  Set<String> peterCategories;
  String slug;
}
