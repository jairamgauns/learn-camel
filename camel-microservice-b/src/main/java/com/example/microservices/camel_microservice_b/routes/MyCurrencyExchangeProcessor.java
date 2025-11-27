package com.example.microservices.camel_microservice_b.routes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.microservices.camel_microservice_b.CurrencyExchange;

@Component
public class MyCurrencyExchangeProcessor {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

	public void processMessage(CurrencyExchange currencyExchange) {
		
		logger.info("Do proceeisng : {}",currencyExchange.getConversionMultiple());
	}
}
