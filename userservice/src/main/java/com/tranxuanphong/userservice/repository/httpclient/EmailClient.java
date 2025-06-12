package com.tranxuanphong.userservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import com.tranxuanphong.userservice.dto.request.EmailRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "emailservice")
public interface EmailClient {
  @PostMapping(value = "/api/email/send2", produces = MediaType.APPLICATION_JSON_VALUE)
  String sendEmail(@RequestBody EmailRequest request);

}
