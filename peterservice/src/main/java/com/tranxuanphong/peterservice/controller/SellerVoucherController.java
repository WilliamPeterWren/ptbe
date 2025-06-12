package com.tranxuanphong.peterservice.controller;

import com.tranxuanphong.peterservice.dto.response.ProductResponse;
import com.tranxuanphong.peterservice.entity.SellerVoucher;
import com.tranxuanphong.peterservice.service.SellerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/seller-vouchers")
public class SellerVoucherController {

    @Autowired
    private SellerVoucherService service;

    @GetMapping
    public ResponseEntity<List<SellerVoucher>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerVoucher> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<SellerVoucher>> getBySellerId(@PathVariable String sellerId) {
        return ResponseEntity.ok(service.getBySellerId(sellerId));
    }

    @PostMapping
    public ResponseEntity<SellerVoucher> create(@RequestBody SellerVoucher voucher) {
        return ResponseEntity.ok(service.create(voucher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerVoucher> update(@PathVariable String id, @RequestBody SellerVoucher voucher) {
        return ResponseEntity.ok(service.update(id, voucher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    
}
