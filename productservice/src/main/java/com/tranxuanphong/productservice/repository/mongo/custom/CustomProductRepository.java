package com.tranxuanphong.productservice.repository.mongo.custom;


import java.util.List;

import com.tranxuanphong.productservice.entity.Product;

public interface CustomProductRepository {
    Product getRandomProduct();
    List<Product> getRandomProducts(int limit);
}

