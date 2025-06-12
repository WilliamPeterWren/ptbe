package com.tranxuanphong.userservice.dto.response;

import java.util.Set;

import com.tranxuanphong.userservice.model.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CartResponse {
  Set<Seller> sellers;
}
