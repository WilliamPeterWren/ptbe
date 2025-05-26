package com.tranxuanphong.productservice.dto.request;


import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {
  @NotBlank(message = "Category name is required")
  @Size(min = 1, max = 32, message = "CATEGORYNAME_INVALID")
  String categoryName;
  
  @NotBlank(message = "Category name is required")
  @Size(min = 1, max = 32, message = "CATEGORYNAME_INVALID")
  Set<String> peterCategories;
}
