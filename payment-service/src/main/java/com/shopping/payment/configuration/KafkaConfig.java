package com.shopping.payment.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic cartClearTopic(){
       return TopicBuilder.name("cart-clear-topic")
                .partitions(10)
                .replicas(3)
                .build();
    }
}
