package com.tranxuanphong.productservice.dto.request;

import lombok.Getter;

@Getter
public class UpdateInfoRequest {

  String productId;
  String name;
  String detail;
}
