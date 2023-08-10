package br.com.compass.pb.blogpass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class Pool {

    @Bean(name = "pool-executer")
    public Executor taskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
