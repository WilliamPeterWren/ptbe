package com.tranxuanphong.orderservice.repository.mongo.custom.impl;

import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.repository.mongo.custom.CustomOrderRepository;
import org.bson.Document; // Import for MongoDB Document
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // Import for Sort
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<Order> findOrdersByUserIdAndStatusSortedByStatusCreatedAt(String userId, String status, Pageable pageable) {
        MatchOperation matchUserAndStatus = match(Criteria.where("userId").is(userId)
                .and("orderStatus.status").is(status));

        UnwindOperation unwindStatus = unwind("orderStatus");

        MatchOperation matchStatus = match(Criteria.where("orderStatus.status").is(status));

        SortOperation sortByStatusCreatedAt = sort(Sort.Direction.DESC, "orderStatus.createdAt");

        GroupOperation groupByOrderId = group("id").first("$$ROOT").as("order");

        ReplaceRootOperation replaceRootWithOrder = replaceRoot("order");

        Aggregation countAgg = newAggregation(
                matchUserAndStatus,
                unwindStatus,
                matchStatus,
                groupByOrderId,
                replaceRootWithOrder,
                count().as("total")
        );

        long total = mongoTemplate.aggregate(countAgg, "orders", Document.class)
                .getUniqueMappedResult() != null ?
                mongoTemplate.aggregate(countAgg, "orders", Document.class).getUniqueMappedResult().getLong("total") :
                0;

        // Final aggregation with pagination
        Aggregation aggregation = newAggregation(
                matchUserAndStatus,
                unwindStatus,
                matchStatus,
                sortByStatusCreatedAt,
                groupByOrderId,
                replaceRootWithOrder,
                skip(pageable.getOffset()),
                limit(pageable.getPageSize())
        );

        List<Order> orders = mongoTemplate.aggregate(aggregation, "orders", Order.class).getMappedResults();

        return new PageImpl<>(orders, pageable, total);
    }
}