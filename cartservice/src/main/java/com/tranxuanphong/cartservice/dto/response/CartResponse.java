package com.tranxuanphong.cartservice.dto.response;

import java.util.Set;

import com.tranxuanphong.cartservice.entity.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CartResponse {
  Set<Seller> sellers;
}
