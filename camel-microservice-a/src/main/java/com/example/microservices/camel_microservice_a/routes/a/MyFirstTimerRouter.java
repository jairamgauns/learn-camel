package com.example.microservices.camel_microservice_a.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {
	
	@Autowired
	GetCurrentTimeBean getCurrentTimeBean;

	/**
	 * Define a route
	 * listen to queue
	 * transformation
	 * processing
	 */
	@Override
	public void configure() throws Exception {
		// timer endpoint
		// transformation
		// log
		//Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
		from("timer:first-timer1")
		.log("${body}")
		.transform().constant("My Constant Message")
		.log("${body}")
		//.transform().constant("Time now is: "+LocalDateTime.now())
		//.bean("getCurrentTimeBean")
		.bean(getCurrentTimeBean,"getCurrentTime")
		.log("${body}")
		.bean(SimpleLoggingProcessingComponent.class)
		.log("${body}")
		.process(new SimpleLoggingProcessor())
		.to("log:first-timerlog");
		
	}

}

@Component
class GetCurrentTimeBean {
	public String getCurrentTime() {
		return "Time now is: "+LocalDateTime.now();
	}
}

@Component
class SimpleLoggingProcessingComponent {
	
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
	public void process(String message) {
		logger.info("SimpleLoggingProcessingComponent {}", message);
	}
}

@Component
class SimpleLoggingProcessor implements Processor {
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("SimpleLoggingProcessor {}", exchange.getMessage().getBody());
	}
}
