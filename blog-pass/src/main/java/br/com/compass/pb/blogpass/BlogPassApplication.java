package br.com.compass.pb.blogpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableFeignClients
@SpringBootApplication
public class BlogPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogPassApplication.class, args);
	}

}
