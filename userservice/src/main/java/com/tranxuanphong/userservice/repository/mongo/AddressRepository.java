package com.tranxuanphong.userservice.repository.mongo;

import com.tranxuanphong.userservice.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {
}
