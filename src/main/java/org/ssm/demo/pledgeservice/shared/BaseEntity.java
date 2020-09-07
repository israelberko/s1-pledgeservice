package org.ssm.demo.pledgeservice.shared;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface BaseEntity {
	final static String PAYLOAD = "payload";
	final static String AFTER = "after";
	final static Logger LOG = LoggerFactory.getLogger(BaseEntity.class);
	final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	default <T> T buildFrom(Map<?,?> changeEvent, Class<T> valueType) {
		Object payload = ((Map<?,?>)changeEvent).get(PAYLOAD);
		
		if ( payload == null ) {
			return OBJECT_MAPPER.convertValue(changeEvent, valueType);
		}
		
		Object afterField = ((Map<?,?>)payload).get(AFTER);
		
		if ( afterField == null ) {
			return OBJECT_MAPPER.convertValue(payload, valueType);
		} 
		
		else {
			return OBJECT_MAPPER.convertValue(afterField, valueType);
		}
	}
	
	default Map<?,?> toMap() {
		return OBJECT_MAPPER.convertValue(this, new TypeReference<Map<?, ?>>() {});
	}
}
