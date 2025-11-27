package com.example.microservices.camel_microservice_a.routes.patterns;

import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.microservices.camel_microservice_a.CurrencyExchange;

@Component
public class EIPPatternsRouter extends RouteBuilder{

	@Autowired
	private DynamicRouterBean dynamicRouterBean;

	@Override
	public void configure() throws Exception {
		
		//getContext().setTracing(true);
		//errorHandler(deadLetterChannel("activemq:dead-letter-queue"));
		
		/*
		 * from("timer:multicast?period=10000") .multicast() .to("log:something1",
		 * "log:something2", "log:something3");
		 */
		
		/*
		 * from("file:files/csv") .unmarshal().csv() .split(body()) .marshal().csv()
		 * .to("log:split-files") .to("activemq:split-queue");
		 */
		
		//Message 1,Messge2,Message3
		from("file:files/csv")
		.convertBodyTo(String.class)
		.split(body(), ",")
		.to("log:split-files")
		.to("activemq:split-queue");
		
		from("file:files/aggregate-json")
		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
		.aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
		.completionSize(3)
		.to("log:aggregate-json");
		
		/*
		 * String routingSlip= "direct:endpoint1,direct:endpoint2";
		 * from("timer:routing-slip?period=10000") .transform().constant("My message")
		 * .routingSlip(simple(routingSlip));
		 */
		
		
		from("direct:endpoint1")
		.wireTap("log:wire-tap")
		.to("{{endpoint-for-logging}}");
		
		from("direct:endpoint2")
		.to("log:directendpoint2");
		
		from("direct:endpoint3")
		.to("log:directendpoint3");
		

		from("timer:dynamic-router?period=10000")
		.transform().constant("My message dynamic")
		.dynamicRouter(method(dynamicRouterBean));
		
	}


}

@Component
class DynamicRouterBean {
	Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
	int invocation;
	public String decideTheNextEndpoint(
			@ExchangeProperties Map<String, String> exchangeProperties,
			@Headers Map<String, String> headers,
			@Body String body) {
		logger.info("{} {} {}", exchangeProperties, headers, body );
		invocation++;
		if(invocation%3==0)
			return "direct:endpoint1";
		if(invocation%3==1)
			return "direct:endpoint2,direct:endpoint3";
		return null;
	}
}
