package org.ssm.demo.partyservice.entity;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssm.demo.partyservice.service.PartyCommandHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface BaseEntity {
	final static String PAYLOAD = "payload";
	final static String AFTER = "after";
	final static Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	default <T> T buildFrom(Map<?,?> changeEvent, Class<T> valueType) {
		LOG.info("Full data: {}", changeEvent);
		Object afterField = ((Map<?,?>)changeEvent.get(PAYLOAD)).get(AFTER);
		return OBJECT_MAPPER.convertValue(afterField, valueType);
	}
	
	default Map<?,?> toMap() {
		return OBJECT_MAPPER.convertValue(this, new TypeReference<Map<?, ?>>() {});
	}
}
