package com.tranxuanphong.peterservice.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;


@Mapper(componentModel = "spring")
public interface FlashSaleMapper {
  FlashSale toFlashSale(FlashSaleCreateRequest request);
  FlashSale updateFlashSale(@MappingTarget FlashSale flashSale, FlashSaleUpdateRequest request);
  FlashSaleResponse toFlashSaleResponse(FlashSale flashSale);
  List<FlashSaleResponse> toListFlashSaleResponse(List<FlashSale> listFlashSale);
}
