package com.tranxuanphong.peterservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PeterCategoryResponse {
  String id;
  String name;
  String parentId;
  String slug;
  String images;
}
