package com.in28minutes.microservices.camel_microservice_a.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqXmlSenderRouter extends RouteBuilder {
    @Override
    public void configure() {
        from("file:files/xml")
                .log("${body}")
                .to("activemq:my-activemq-xml-queue");
    }
}
