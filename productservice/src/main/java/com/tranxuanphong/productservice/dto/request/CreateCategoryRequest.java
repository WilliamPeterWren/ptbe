package com.tranxuanphong.productservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

  @NotBlank(message = "Seller ID is required")
  @Size(min = 1, max = 32, message = "SELLERID_INVALID")
  String sellerId;

  
  @NotBlank(message = "Category name is required")
  @Size(min = 1, max = 32, message = "CATEGORYNAME_INVALID")
  String categoryName;

  String slug;
}
