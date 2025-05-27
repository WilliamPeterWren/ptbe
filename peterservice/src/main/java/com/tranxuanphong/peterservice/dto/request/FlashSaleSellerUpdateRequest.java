package com.tranxuanphong.peterservice.dto.request;

import java.util.Set;

import com.tranxuanphong.peterservice.entity.FlashSaleItem;

import lombok.Getter;

@Getter
public class FlashSaleSellerUpdateRequest {
  Set<FlashSaleItem> flashSaleItems;
}
