package com.tranxuanphong.productservice.repository.mongo.custom;

import com.tranxuanphong.productservice.entity.Product;
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Product getRandomProduct() {
        List<Product> result = mongoTemplate.aggregate(
                org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation(
                        org.springframework.data.mongodb.core.aggregation.Aggregation.sample(1)
                ),
                "product", 
                Product.class
        ).getMappedResults();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Product> getRandomProducts(int limit) {
        Aggregation agg = newAggregation(sample(limit));
        return mongoTemplate.aggregate(agg, "products", Product.class).getMappedResults();
    }

}
