package org.ssm.demo.partyservice.entity;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Entity {
	final static String PAYLOAD = "payload";
	final static String AFTER = "after";
	
	default <T> T withAfterField(Map<?,?> changeEvent, Class<T> valueType) {
		ObjectMapper objectMapper = new ObjectMapper();
		Object afterField = ((Map<?,?>)changeEvent.get(PAYLOAD)).get(AFTER);
		return objectMapper.convertValue(afterField, valueType);
	}
}
