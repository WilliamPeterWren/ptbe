package com.tranxuanphong.productservice.dto.request;

import java.util.HashSet;
import java.util.Set;

import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Variant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

  @NotBlank(message = "product name is required")
  @Size(min = 1, max = 32, message = "PRODUCT_INVALID")
  String productName;

  @NotBlank(message = "CATEGORY name is required")
  @Size(min = 1, max = 32, message = "CATEGORYID_INVALID")
  String categoryId;
  
  String peterCategory;

  Set<String> productImages;

  @NotBlank(message = "description name is required")
  @Size(min = 1, max = 32, message = "DESCRIPTION_INVALID")
  String description;


  @NotEmpty(message = "At least one variant is required")
  @Valid
  Set<Variant> variants;

  @NotEmpty(message = "At least one info is required")
  @Valid
  Set<Info> infos;

  String shippingId;
}
