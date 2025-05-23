package com.tranxuanphong.productservice.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateProductRequest {
  @NotBlank(message = "Seller ID is required")
  @Size(min = 1, max = 32, message = "SELLERID_INVALID")
  String sellerId;

  String productName;

  String categoryId;

  Set<String> productImages;

  String description;
}
