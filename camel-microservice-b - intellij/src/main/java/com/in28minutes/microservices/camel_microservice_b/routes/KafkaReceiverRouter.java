package com.in28minutes.microservices.camel_microservice_b.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class KafkaReceiverRouter extends RouteBuilder {

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    @Override
    public void configure() {

        from("kafka:myKafkaTopic?brokers=localhost:9092")
                .to("log:received-message-from-kafka");

    }
}

