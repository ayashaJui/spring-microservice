package com.photoApp.ApiGateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilterConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

    @Order(1)
    @Bean
    public GlobalFilter secondGlobalFilter(){
        return (exchange, chain) -> {
            logger.info("Second Pre Filter executed..");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Second Post Filter executed..");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdGlobalFilter(){
        return (exchange, chain) -> {
            logger.info("Third Pre Filter executed..");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Third Post Filter executed..");
            }));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter fourthGlobalFilter(){
        return (exchange, chain) -> {
            logger.info("Fourth Pre Filter executed..");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Fourth Post Filter executed..");
            }));
        };
    }
}
