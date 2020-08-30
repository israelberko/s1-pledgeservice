package org.ssm.demo.partyservice.entity;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssm.demo.partyservice.service.PartyCommandHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface BaseEntity {
	final static String PAYLOAD = "payload";
	final static String AFTER = "after";
	final static Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	
	default <T> T buildFrom(Map<?,?> changeEvent, Class<T> valueType) {
		ObjectMapper objectMapper = new ObjectMapper();
		LOG.info("Full data: {}", changeEvent);
		Object afterField = ((Map<?,?>)changeEvent.get(PAYLOAD)).get(AFTER);
		return objectMapper.convertValue(afterField, valueType);
	}
}
