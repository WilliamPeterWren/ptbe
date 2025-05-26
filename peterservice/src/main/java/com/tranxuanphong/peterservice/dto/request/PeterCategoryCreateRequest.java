package com.tranxuanphong.peterservice.dto.request;


import lombok.Getter;

@Getter
public class PeterCategoryCreateRequest {
  String name;
  String parentId;
  String images;
}
