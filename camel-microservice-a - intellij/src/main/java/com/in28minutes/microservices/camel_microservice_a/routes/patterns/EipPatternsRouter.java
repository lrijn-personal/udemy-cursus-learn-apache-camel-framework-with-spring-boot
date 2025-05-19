package com.in28minutes.microservices.camel_microservice_a.routes.patterns;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    private SplitterComponent splitterComponent;

    @Autowired
    private DynamicRouterBean dynamicRouter;

    @Override
    public void configure() {

        errorHandler(deadLetterChannel("activemq:dead-letter-queue"));

//        from("timer:multicast?period=10000")
////                .multicast()
//                .to("log:something1", "log:something2");

//        from("file:files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .marshal().json(JsonLibrary.Jackson)
//                .log("${body}")
//                .to("activemq:split-queue");

        //message, message2, message3
//        from("file:files/csv")
//                .convertBodyTo(String.class)
//                .split(body(),",")
//                .log("${body}")
//                .to("activemq:split-queue");




        from("file:files/csv")
                .split(method(splitterComponent))
                .log("${body}")
                .to("activemq:split-queue");

        from("file:files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                .completionSize(3)
                .to("log:aggregate-json");

        //routingSlip pattern
        String routingSlip = "direct:endpoint1,direct:endpoint2,direct:endpoint3";
//        from("timer:routingSlip?period=10000")
//                .transform().constant("My Message is hardcoded")
//                .routingSlip(simple(routingSlip));

//        getContext().setTracing(true);

        from("direct:endpoint1")
                .wireTap("log:wiretapped") //additional endpoint
                .to("{{endpoint-for-logging}}");
        from("direct:endpoint2")
                .to("log:directendpoint2");
        from("direct:endpoint3")
                .to("log:directendpoint3");

        //Dynamic Routing
        from("timer:dynamicRouter?period={{timePeriod}}")
                .transform().constant("My message is Hardcoded")
                .dynamicRouter(method(dynamicRouter));



    }
}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return List.of("ABC","DEF", "GHI");
    }
}

@Component
class DynamicRouterBean{
    Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);

    int invocations;

    public String decideTheNextEndpoint(@ExchangeProperties Map<String, String> properties,
                                        @Headers Map<String, String> headers,
                                        @Body String body){
        logger.info("{} {} {}", properties, headers, body);
        invocations++;
        if(invocations%3 == 0){
            return "direct:endpoint1";
        }

        if(invocations%3 == 1){
            return "direct:endpoint2,direct:endpoint3";
        }

        return null;
    }
}

