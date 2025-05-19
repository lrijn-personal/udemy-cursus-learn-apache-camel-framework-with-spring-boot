package com.in28minutes.microservices.camel_microservice_b.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqXmlReceiverRouter extends RouteBuilder {

    @Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    @Override
    public void configure() {
        from("activemq:my-activemq-xml-queue")
                .unmarshal().jacksonXml(CurrencyExchange.class)
                .bean(myCurrencyExchangeProcessor)
                .bean(myCurrencyExchangeTransformer)
                .bean(myCurrencyExchangeProcessor)
                .to("log:received-xml-message-from-activemq");
    }
}

@Component
class MyCurrencyExchangeProcessor {
    public void logConversion(CurrencyExchange currencyExchange){
        System.out.println("current conversionMultiple: " + currencyExchange.getConversionMultiple());
    }
}
