package com.tranxuanphong.peterservice.controller;

import com.tranxuanphong.peterservice.entity.ShippingVoucher;
import com.tranxuanphong.peterservice.service.ShippingVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-vouchers")
public class ShippingVoucherController {

    @Autowired
    private ShippingVoucherService service;

    @GetMapping
    public ResponseEntity<List<ShippingVoucher>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/user/getall")
    public ResponseEntity<List<ShippingVoucher>> userGetAll() {
        return ResponseEntity.ok(service.userGetAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingVoucher> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ShippingVoucher getByIdd(@PathVariable String id) {
        return service.getByIdd(id);
    }

    @PostMapping("/count/{count}")
    public ResponseEntity<ShippingVoucher> create(@RequestBody ShippingVoucher voucher, @PathVariable int count) {
        return ResponseEntity.ok(service.create(voucher, count));
    }

    @PutMapping("/{id}/count/{count}")
    public ResponseEntity<ShippingVoucher> update(@PathVariable String id, @PathVariable int count, @RequestBody ShippingVoucher voucher) {
        return ResponseEntity.ok(service.update(id, count, voucher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/update/all")
    public void adminUpdateAll(){
        service.adminUpdateAll();
    }
}

