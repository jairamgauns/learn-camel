package com.example.microservices.camel_microservice_a.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		/*
		 * from("timer:activemq-timer?period=10000")
		 * .transform().constant("My message for Active MQ") .log("${body}")
		 * .to("activemq:my-activemq-queue");
		 */

	
		/*
		 * from("file:files/json") .log("${body}") .to("activemq:my-activemq-queue");
		 */
		
		from("file:files/xml")
		.log("${body}")
		.to("activemq:my-activemq-xml-queue");
	}

}
