package com.in28minutes.microservices.camel_microservice_a.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class KafkaSenderRouter extends RouteBuilder {
    @Override
    public void configure() {
        from("file:files/json")
                .log("${body}")
                .to("kafka:myKafkaTopic?brokers=localhost:9092");
    }
}
