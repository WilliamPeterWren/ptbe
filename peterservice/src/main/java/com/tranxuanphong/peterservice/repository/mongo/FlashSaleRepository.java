package com.tranxuanphong.peterservice.repository.mongo;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.repository.custom.FlashSaleRepositoryCustom;

@Repository
public interface FlashSaleRepository extends MongoRepository<FlashSale, String>, FlashSaleRepositoryCustom  {
  boolean existsByName(String name);

  List<FlashSale> findByExpiredAtAfter(Instant now);

  @Query(value = "{ '_id': ?0, 'flashSaleItems.sellerId': ?1 }", fields = "{ 'flashSaleItems': { $elemMatch: { 'sellerId': ?1 } } }")
  Optional<FlashSale> findFlashSaleItemsByIdAndSellerId(String flashSaleId, String sellerId);

  @Aggregation(pipeline = {
        "{ $match: { '_id': ?0, 'flashSaleItems.sellerId': ?1 } }",
        "{ $unwind: '$flashSaleItems' }",
        "{ $match: { 'flashSaleItems.sellerId': ?1 } }",
        "{ $sort: { 'flashSaleItems.updatedAt': -1 } }",
        "{ $group: { _id: '$_id', name: { $first: '$name' }, slug: { $first: '$slug' }, startedAt: { $first: '$startedAt' }, expiredAt: { $first: '$expiredAt' }, flashSaleItems: { $push: '$flashSaleItems' } } }"
  })
  Optional<FlashSale> findFlashSaleItemsByIdAndSellerIdSortedByUpdatedAtDesc(String flashSaleId, String sellerId);

  @Aggregation(pipeline = {
    "{ $match: { '_id': ?0, 'flashSaleItems.productId': ?1 } }",
    "{ $unwind: '$flashSaleItems' }",
    "{ $match: { 'flashSaleItems.productId': ?1 } }",
    "{ $sort: { 'flashSaleItems.updatedAt': -1 } }",
    "{ $group: { _id: '$_id', name: { $first: '$name' }, slug: { $first: '$slug' }, startedAt: { $first: '$startedAt' }, expiredAt: { $first: '$expiredAt' }, flashSaleItems: { $push: '$flashSaleItems' } } }"
  })
  Optional<FlashSale> findFlashSaleItemsByIdAndProductIdSortedByUpdatedAtDesc(String flashSaleId, String productId);

  List<FlashSale> findByAvailableTrueAndExpiredAtBefore(Instant time);
}
