package br.com.compass.pb.blogpass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolTaskExecutor {

    @Bean(name = "pool-executer")
    public Executor taskExecutor() {
        return new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor();
    }

}
