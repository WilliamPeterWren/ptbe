package com.tranxuanphong.productservice.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.productservice.dto.request.CreateInfoRequest;
import com.tranxuanphong.productservice.dto.request.UpdateInfoRequest;
import com.tranxuanphong.productservice.dto.response.InfoResponse;
import com.tranxuanphong.productservice.entity.Info;


@Mapper(componentModel = "spring") 
public interface InfoMapper {

  Info toInfo(CreateInfoRequest request); 
  void updateInfo(@MappingTarget Info info, UpdateInfoRequest request); 
  InfoResponse toInfoResponse(Info info); 
  List<InfoResponse> toListInfoResponse(List<Info> listInfo);
}
