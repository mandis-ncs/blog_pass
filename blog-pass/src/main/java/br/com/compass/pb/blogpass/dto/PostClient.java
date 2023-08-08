package br.com.compass.pb.blogpass.dto;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "post-feign-client", url = "https://jsonplaceholder.typicode.com")
public interface PostClient {


}
