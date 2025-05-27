package com.tranxuanphong.peterservice.controller;

import com.tranxuanphong.peterservice.dto.request.PeterVoucherCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterVoucherUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.ApiResponse;
import com.tranxuanphong.peterservice.dto.response.PeterVoucherResponse;
import com.tranxuanphong.peterservice.entity.PeterVoucher;
import com.tranxuanphong.peterservice.service.PeterVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class PeterVoucherController {

    @Autowired
    private PeterVoucherService peterVoucherService;

    @GetMapping
    public ApiResponse<List<PeterVoucherResponse>> getAll() {
        return ApiResponse.<List<PeterVoucherResponse>>builder()
        .code(200)
        .result(peterVoucherService.getAll())
        .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PeterVoucherResponse> getById(@PathVariable String id) {
        return ApiResponse.<PeterVoucherResponse>builder()
        .code(200)
        .result(peterVoucherService.getById(id))
        .build();
    }

    @PostMapping
    public ApiResponse<PeterVoucherResponse> create(@RequestBody PeterVoucherCreateRequest request) {
        return ApiResponse.<PeterVoucherResponse>builder()
        .code(200)
        .result(peterVoucherService.create(request))
        .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PeterVoucherResponse> update(@PathVariable String id, @RequestBody PeterVoucherUpdateRequest request) {
        return ApiResponse.<PeterVoucherResponse>builder()
        .code(200)
        .result(peterVoucherService.update(id, request))
        .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        peterVoucherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
