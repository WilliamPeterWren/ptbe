package com.tranxuanphong.peterservice.controller;


import com.tranxuanphong.peterservice.entity.ProductVoucher;
import com.tranxuanphong.peterservice.service.ProductVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-vouchers")
public class ProductVoucherController {

    @Autowired
    private ProductVoucherService service;

    @GetMapping
    public ResponseEntity<List<ProductVoucher>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVoucher> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/variant/{variantId}")
    public ResponseEntity<List<ProductVoucher>> getByVariantId(@PathVariable String variantId) {
        return ResponseEntity.ok(service.getByVariantId(variantId));
    }

    @PostMapping
    public ResponseEntity<ProductVoucher> create(@RequestBody ProductVoucher voucher) {
        return ResponseEntity.ok(service.create(voucher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVoucher> update(@PathVariable String id, @RequestBody ProductVoucher voucher) {
        return ResponseEntity.ok(service.update(id, voucher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
