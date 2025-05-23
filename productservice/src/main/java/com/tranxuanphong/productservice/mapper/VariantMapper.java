package com.tranxuanphong.productservice.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.productservice.dto.request.CreateVariantRequest;
import com.tranxuanphong.productservice.dto.request.UpdateVariantRequest;
import com.tranxuanphong.productservice.dto.response.VariantResponse;
import com.tranxuanphong.productservice.entity.Variant;


@Mapper(componentModel = "spring")
public interface VariantMapper {
  Variant toVariant(CreateVariantRequest request); 
  void updateVariant(@MappingTarget Variant variant, UpdateVariantRequest request); 
  VariantResponse toVariantResponse(Variant variant); 
  List<VariantResponse> toListVariantResponse(List<Variant> listVariant);
}
