package com.tranxuanphong.peterservice.dto.response;

import java.time.Instant;
import java.util.Set;

import com.tranxuanphong.peterservice.entity.FlashSaleItem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FlashSaleResponse {
  String id;
  String name;
  String slug;

  Boolean available;

  Set<FlashSaleItem> flashSaleItems;
  Instant startedAt;
  Instant expiredAt;
}
