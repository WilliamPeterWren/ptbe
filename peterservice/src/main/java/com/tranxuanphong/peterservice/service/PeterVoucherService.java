package com.tranxuanphong.peterservice.service;

import com.tranxuanphong.peterservice.dto.request.PeterVoucherCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterVoucherUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.PeterVoucherResponse;
import com.tranxuanphong.peterservice.entity.PeterVoucher;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.PeterVoucherMapper;
import com.tranxuanphong.peterservice.repository.mongo.PeterVoucherRepository;
import com.tranxuanphong.peterservice.service.PeterVoucherService;
import com.tranxuanphong.peterservice.utils.GenerateSlug;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PeterVoucherService {

    PeterVoucherRepository peterVoucherRepository;
    PeterVoucherMapper peterVoucherMapper;
    GenerateSlug generateSlug;

    public List<PeterVoucherResponse> getAll() {
        Instant now = Instant.now();
        List<PeterVoucher> list = peterVoucherRepository.findByExpiredAtAfter(now);
        return peterVoucherMapper.toListPeterVoucherResponse(list);
    }

    public PeterVoucherResponse getById(String id) {
        PeterVoucher peterVoucher = peterVoucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PETER_VOUCHER_NOT_FOUND));

        return peterVoucherMapper.toPeterVoucherResponse(peterVoucher);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public PeterVoucherResponse create(PeterVoucherCreateRequest request) {
        PeterVoucher peterVoucher = peterVoucherMapper.toPeterVoucher(request);
        String slug = generateSlug.generateSlug(request.getName());
        peterVoucher.setSlug(slug);
        return peterVoucherMapper.toPeterVoucherResponse(peterVoucherRepository.save(peterVoucher));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public PeterVoucherResponse update(String id, PeterVoucherUpdateRequest request) {
        PeterVoucher peterVoucher = peterVoucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PETER_VOUCHER_NOT_FOUND));
        
        peterVoucherMapper.updatePeterVoucher(peterVoucher, request);
        
        return peterVoucherMapper.toPeterVoucherResponse(peterVoucherRepository.save(peterVoucher));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public void delete(String id) {
        peterVoucherRepository.deleteById(id);
    }
}
