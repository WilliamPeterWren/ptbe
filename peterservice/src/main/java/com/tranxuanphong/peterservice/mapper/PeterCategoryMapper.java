package com.tranxuanphong.peterservice.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.peterservice.dto.request.PeterCategoryCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterCategoryUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.PeterCategoryResponse;
import com.tranxuanphong.peterservice.entity.PeterCategory;


@Mapper(componentModel = "spring")
public interface PeterCategoryMapper {
  PeterCategory toPeterCategory(PeterCategoryCreateRequest request);
  void updatePeterCategory(@MappingTarget PeterCategory peterCategory, PeterCategoryUpdateRequest request);
  PeterCategoryResponse toPeterCategoryResponse(PeterCategory peterCategory);
  List<PeterCategoryResponse> toListPeterCategoryResponse(List<PeterCategory> listPeterCategory);
}
