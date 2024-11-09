package com.test.kafka.testOne.service;

import com.test.kafka.testOne.service.dto.CreateProductDto;
import com.test.kafka.testOne.service.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDto createProductDto) throws ExecutionException, InterruptedException {

        //TODO  save to dataBase
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, createProductDto.getTitle(),
                createProductDto.getPrice(), createProductDto.getQuantity());
        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send("product-create-eventss-topic", productId, productCreatedEvent).get();

        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());




//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate
//                .send("product-create-eventss-topic", productId, productCreatedEvent);
//
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                LOGGER.error("Failed to send message: {}", exception.getMessage());
//            } else {
//                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
//            }
//        });
//      TODO ЭТО НЕИСПОЛЬЗУЕТСЯ!!!

        LOGGER.info("Return {}", productId);

        return productId;
    }
}
