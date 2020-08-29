package org.ssm.demo.partyservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
public class ConsumerConfig {
	@Bean public JsonMessageConverter messageConverter() {
		return new BytesJsonMessageConverter();
	}
}
