package com.tranxuanphong.peterservice.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GenerateUUID {
  public String generateUuid() {
    return UUID.randomUUID().toString();
}
}
