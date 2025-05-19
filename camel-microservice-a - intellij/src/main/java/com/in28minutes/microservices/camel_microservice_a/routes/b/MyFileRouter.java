package com.in28minutes.microservices.camel_microservice_a.routes.b;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.camel.Headers;
import java.util.Map;

//@Component
public class MyFileRouter extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {
        from("file:files/input")
//                .pipeline
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice()
                    .when(simple("${file:ext} == 'xml'"))
                        .log("XML FILE")
//                    .when(simple("${body} contains 'USD'"))
                    .when(method(deciderBean))
                        .log("Not an XML FILE BUT contains USD")
                    .otherwise()
                        .log("NOT an XML FILE")
                .end ()
                .to("direct:log-files-value")
                .to("file:files/output");

        from("direct:log-files-value")
//                .log("${messageHistory} ${headers.CamelFileAbsolute} ${file:absolute.path}")
                .log("file name: ${file:name}")
                .log("file extension: ${file:ext}");
    }

}

@Component
class DeciderBean {

    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Body String body,
                                      @Headers Map<String,String> headers,
                                      @ExchangeProperties Map<String,String> exchangeProperties) {
        logger.info("DeciderBean {}, {}, {}.", body, headers, exchangeProperties);
        return true;
    }
}