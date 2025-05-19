package com.in28minutes.microservices.camel_microservice_a.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSendFileRouter extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            //timer
            // queue
            from("file:files/json")
                    .log("${body}")
                    .to("activemq:my-activemq-queue");
        }

    }



