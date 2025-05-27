package com.tranxuanphong.peterservice.dto.request;

import java.time.Instant;


import lombok.Getter;

@Getter
public class PeterVoucherCreateRequest {
  String name;
  Long value;
  Instant expiredAt;
}
