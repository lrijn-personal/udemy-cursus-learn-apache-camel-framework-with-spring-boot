package com.in28minutes.microservices.camel_microservice_a.routes.d;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder {

    @Override
    public void configure() {

        restConfiguration().host("localhost").port(8070);
        from("timer:rest-api-consumer?period=10000")
                .setHeader("from", constant("EUR"))
//                .setHeader("from", () -> "EUR")
                .setHeader("to", () -> "INR")
                .to("rest:get:/currency-exchange/from/{from}/to/{to}")
                .log("${body}");
    }
}
