package com.tranxuanphong.peterservice.repository.custom;

import java.util.List;

import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.entity.FlashSaleItem;

public interface FlashSaleRepositoryCustom {
  List<FlashSaleItem> getFlashSaleItems(String flashSaleId, int page, int size);
  List<FlashSale> getActiveFlashSales();

}

