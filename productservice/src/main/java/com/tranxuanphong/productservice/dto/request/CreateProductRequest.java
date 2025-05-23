package com.tranxuanphong.productservice.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
  @NotBlank(message = "Seller ID is required")
  @Size(min = 1, max = 32, message = "SELLERID_INVALID")
  String sellerId;

  @NotBlank(message = "product name is required")
  @Size(min = 1, max = 32, message = "PRODUCT_INVALID")
  String productName;

  @NotBlank(message = "CATEGORY name is required")
  @Size(min = 1, max = 32, message = "CATEGORYID_INVALID")
  String categoryId;

  Set<String> productImages;

  String slug;

  @NotBlank(message = "description name is required")
  @Size(min = 1, max = 32, message = "DESCRIPTION_INVALID")
  String description;
}
