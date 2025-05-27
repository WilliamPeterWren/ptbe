package com.tranxuanphong.peterservice.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.peterservice.dto.request.PeterVoucherCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterVoucherUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.PeterVoucherResponse;
import com.tranxuanphong.peterservice.entity.PeterVoucher;


@Mapper(componentModel = "spring")
public interface PeterVoucherMapper {
  PeterVoucher toPeterVoucher(PeterVoucherCreateRequest request);
  PeterVoucher updatePeterVoucher(@MappingTarget PeterVoucher peterVoucher, PeterVoucherUpdateRequest request);
  PeterVoucherResponse toPeterVoucherResponse(PeterVoucher peterVoucher);
  List<PeterVoucherResponse> toListPeterVoucherResponse(List<PeterVoucher> listPeterVoucher);
}
