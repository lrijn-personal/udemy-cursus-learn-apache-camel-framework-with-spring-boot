package com.in28minutes.microservices.camel_microservice_b.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    @Override
    public void configure() {

        from("activemq:my-activemq-queue")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .bean(myCurrencyExchangeTransformer)
                .to("log:received-message-from-activemq");

    }
}

@Component
class MyCurrencyExchangeTransformer {

    public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {

        currencyExchange.setConversionMultiple(
                currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN)
        );
        return currencyExchange;

    }
}