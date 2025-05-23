package com.in28minutes.microservices.camel_microservice_a.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        //timer
        // queue
         from("timer:active-mq-timer?period=10000")
                 .transform().constant("My message for Active MQ")
                 .log("${body}")
                .to("activemq:my-activemq-queue");
    }

}


