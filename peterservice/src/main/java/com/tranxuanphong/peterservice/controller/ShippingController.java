package com.tranxuanphong.peterservice.controller;

import com.tranxuanphong.peterservice.entity.Shipping;
import com.tranxuanphong.peterservice.service.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippings")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping
    public ResponseEntity<List<Shipping>> getAllShippings() {
        return ResponseEntity.ok(shippingService.getAllShippings());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Shipping>> adminGetAllShippings() {
        return ResponseEntity.ok(shippingService.adminGetAllShippings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipping> getShippingById(@PathVariable String id) {
        return shippingService.getShippingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public Shipping getShippingByIdd(@PathVariable String id) {
        return shippingService.getShippingByIdd(id);
    }

    @PostMapping
    public ResponseEntity<Shipping> createShipping(@RequestBody Shipping shipping) {
        return ResponseEntity.ok(shippingService.createShipping(shipping));
    }

    @PutMapping("/{id}")
    public Shipping updateShipping(@PathVariable String id, @RequestBody Shipping shipping) {
        System.out.println("controller.....");
        return shippingService.updateShipping(id, shipping);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipping(@PathVariable String id) {
        shippingService.deleteShipping(id);
        return ResponseEntity.noContent().build();
    }
}
