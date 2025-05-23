package com.tranxuanphong.cartservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCartResponse {
  String cartId; 
  String userId;
}
