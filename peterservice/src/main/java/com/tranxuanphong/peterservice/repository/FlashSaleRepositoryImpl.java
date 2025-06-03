package com.tranxuanphong.peterservice.repository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.entity.FlashSaleItem;
import com.tranxuanphong.peterservice.repository.custom.FlashSaleRepositoryCustom;

@Repository
public class FlashSaleRepositoryImpl implements FlashSaleRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<FlashSaleItem> getFlashSaleItems(String flashSaleId, int page, int size) {
        Query query = new Query(Criteria.where("_id").is(flashSaleId));
        query.fields().include("flashSaleItems");

        FlashSale flashSale = mongoTemplate.findOne(query, FlashSale.class);

        if (flashSale == null || flashSale.getFlashSaleItems() == null) {
            return Collections.emptyList();
        }

        return flashSale.getFlashSaleItems()
                .stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlashSale> getActiveFlashSales() {
        Query query = new Query(Criteria.where("expiredAt").gt(Instant.now()));
        return mongoTemplate.find(query, FlashSale.class);
    }

    @Override
    public FlashSaleItem findFlashSaleItemByProductId(String productId) {
        Query query = new Query(Criteria.where("flashSaleItems.productId").is(productId));
        FlashSale flashSale = mongoTemplate.findOne(query, FlashSale.class);

        if (flashSale != null) {
            return flashSale.getFlashSaleItems()
                            .stream()
                            .filter(item -> productId.equals(item.getProductId()))
                            .findFirst()
                            .orElse(null);
        }

        return null;
    }

}

