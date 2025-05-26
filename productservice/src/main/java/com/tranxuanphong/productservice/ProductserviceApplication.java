package com.tranxuanphong.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
// @EnableMongoRepositories(basePackages = "com.tranxuanphong.productservice.repository.mongo")
// @EnableElasticsearchRepositories(basePackages = "com.tranxuanphong.productservice.repository.elasticsearch")
public class ProductserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductserviceApplication.class, args);
	}

}
