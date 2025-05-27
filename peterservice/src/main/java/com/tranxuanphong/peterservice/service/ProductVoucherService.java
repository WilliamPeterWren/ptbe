package com.tranxuanphong.peterservice.service;


import com.tranxuanphong.peterservice.entity.ProductVoucher;
import com.tranxuanphong.peterservice.repository.mongo.ProductVoucherRepository;
import com.tranxuanphong.peterservice.service.ProductVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVoucherService {

    @Autowired
    private ProductVoucherRepository repository;

    public List<ProductVoucher> getAll() {
        return repository.findAll();
    }

    public Optional<ProductVoucher> getById(String id) {
        return repository.findById(id);
    }

    public List<ProductVoucher> getByVariantId(String variantId) {
        return repository.findByVariantId(variantId);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ProductVoucher create(ProductVoucher voucher) {
        return repository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ProductVoucher update(String id, ProductVoucher voucher) {
        if (!repository.existsById(id)) throw new RuntimeException("Voucher not found");
        voucher.setId(id);
        return repository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {
        repository.deleteById(id);
    }
}
