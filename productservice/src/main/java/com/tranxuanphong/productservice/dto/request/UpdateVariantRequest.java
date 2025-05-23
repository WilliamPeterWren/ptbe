package com.tranxuanphong.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateVariantRequest {

  @NotBlank(message = "product id is required")
  @Size(min = 1, max = 32, message = "PRODUCTID_INVALID")
  String productId;
  
  @NotBlank(message = "variant name is required")
  @Size(min = 1, max = 32, message = "VARIANT_NAME_INVALID")
  String variantName;

  @NotBlank(message = "variant price is required")
  @Size(min = 1, message = "PRICE_INVALID")
  Long price;

  @NotBlank(message = "variant stock is required")
  @Size(min = 1, message = "STOCK_INVALID")
  Long stock;
}
