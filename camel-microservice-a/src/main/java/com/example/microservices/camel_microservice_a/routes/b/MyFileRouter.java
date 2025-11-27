package com.example.microservices.camel_microservice_a.routes.b;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Header;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder{
	
	@Autowired
	private DeciderBean deciderBean;

	@Override
	public void configure() throws Exception {
		from("file:files/input")
		.routeId("file-input-route")
		.transform().body(String.class)
		.choice()
			.when(simple("${file:ext} ends with 'xml' "))
				.log("Xml file")
			//.when(simple("${body} contains 'USD' "))
			  .when(method(deciderBean))	
				.log("Not an Xml File but contains USD")
			.otherwise()
				.log("Not an Xml File")
		.end()
	//	.log("${messageHistory}")
	//	.to("direct:log-file-values")
		.to("file:files/output");
		
		from ("direct:log-file-values")
		.log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}");
	}

}

@Component
class DeciderBean {
	
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	
	public boolean isThisConditionMet(@Headers Map<String,String> headers, 
			@Body String body, @ExchangeProperties Map<String,String> exchangeProperties) {
		logger.info("Bean: {} {} {}", headers, exchangeProperties, body);		
		return true;
	}
}
