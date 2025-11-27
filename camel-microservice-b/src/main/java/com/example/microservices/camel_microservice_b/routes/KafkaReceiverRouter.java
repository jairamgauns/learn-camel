package com.example.microservices.camel_microservice_b.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiverRouter extends RouteBuilder{
	
	@Override
	public void configure() throws Exception {
		
		from("kafka:my-kafka-topic")
		.log("${body}")
		.to("log:receiver-kafka:my-kafka-topic");
		
	}

}

