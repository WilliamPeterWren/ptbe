package com.tranxuanphong.userservice.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SellerInfoResponse {
  String sellerId;
  String sellerUsername;
  int countProduct;
  LocalDate createdAt;
  int follower;
  int following;
  int rating;
  double star;
  String avatar;
}
