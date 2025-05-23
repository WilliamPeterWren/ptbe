package com.tranxuanphong.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInfoRequest {
  
  @NotBlank(message = "product id is required")
  @Size(min = 1, max = 32, message = "PRODUCTID_INVALID")
  String productId;
  
  @NotBlank(message = "info name is required")
  @Size(min = 1, max = 32, message = "INFO_NAME_INVALID")
  String name;

  @NotBlank(message = "info detail is required")
  @Size(min = 1, max = 32, message = "INFO_DETAIL_INVALID")
  String detail;
}
