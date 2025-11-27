package com.example.microservices.camel_microservice_b.routes;

import java.math.BigDecimal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.microservices.camel_microservice_b.CurrencyExchange;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder{

	@Autowired
	MyCurrencyExchangeProcessor1 myCurrencyExchangeProcessor;

	@Autowired
	MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
	
	@Override
	public void configure() throws Exception {
		/*
		 * from("activemq:my-activemq-queue") .unmarshal().json(JsonLibrary.Jackson,
		 * CurrencyExchange.class) .log("${body}") .bean(myCurrencyExchangeProcessor,
		 * "processMessage") .bean(myCurrencyExchangeTransformer)
		 * .to("log:receiver-mq");
		 */
		
		/*
		 * from("activemq:my-activemq-xml-queue")
		 * .unmarshal().jacksonXml(CurrencyExchange.class) .log("${body}")
		 * .to("log:receiver-activemq-xml-queue");
		 */
		
		from("activemq:split-queue")
		.to("log:receiver-activemq-split-queue");
		
	}

}

@Component
class MyCurrencyExchangeProcessor1 {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

	public void processMessage(CurrencyExchange currencyExchange) {
		
		logger.info("Do proceeisng : {}",currencyExchange.getConversionMultiple());
	}
}

@Component
class MyCurrencyExchangeTransformer {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeTransformer.class);

	public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {
		currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
		return currencyExchange;
	}
}

