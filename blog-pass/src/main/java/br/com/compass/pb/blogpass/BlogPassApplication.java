package br.com.compass.pb.blogpass;

import br.com.compass.pb.blogpass.messaging.producers.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableAsync
@SpringBootApplication
public class BlogPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogPassApplication.class, args);
	}

}
