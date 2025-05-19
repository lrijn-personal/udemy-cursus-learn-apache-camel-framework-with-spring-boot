package com.in28minutes.microservices.camel_microservice_b.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqReceiveCsvRouter extends RouteBuilder {
    @Override
    public void configure() {

        from("activemq:split-queue")
                .to("log:received-message-from-active-mq");

    }
}
