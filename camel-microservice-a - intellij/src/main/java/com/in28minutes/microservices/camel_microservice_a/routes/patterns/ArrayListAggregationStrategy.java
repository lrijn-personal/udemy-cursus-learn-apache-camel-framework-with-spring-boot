package com.in28minutes.microservices.camel_microservice_a.routes.patterns;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


public class ArrayListAggregationStrategy implements AggregationStrategy {
    //1,2,3

    //null, 1 => [1]
    //[1], 2 => [1,2]
    //[1,2], 3 => [1,2,3]

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Object newBody = newExchange.getIn().getBody();
        ArrayList<Object> list = null;
        if (oldExchange == null) {
            list = new ArrayList<Object>();
            list.add(newBody);
            newExchange.getIn().setBody(list);
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(ArrayList.class);
            list.add(newBody);
            return oldExchange;
        }
    }
}
