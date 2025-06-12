package com.tranxuanphong.productservice.repository.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fileservice")
public interface FileClient {

  @DeleteMapping(value = "/api/files/product", produces = MediaType.APPLICATION_JSON_VALUE)
  void deleteFilesProduct(@RequestBody List<String> files);

}
