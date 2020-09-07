package org.ssm.demo.pledgeservice.shared;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;


@Component
public class Utils {
	public String getAsString(Map<?,?> map, String key) {
		Object value = map.get(key);
		
		return String.valueOf(value);
	}
	
	public Integer getAsInt(Map<?,?> map, String key) {
		String value = this.getAsString(map, key);
		
		return StringUtils.isNumeric(value) ? NumberUtils.parseNumber(value,Integer.class) : null;
	}
	
	public <T> T getExtendedStateVar(StateContext<?, ?> context, String var, Class<T> valueType) {
		return context.getExtendedState().get(var,  valueType);
	}
	
	public String getExtendedStateVarAsString(StateContext<?, ?> context, String var) {
		Object value = context.getExtendedState().getVariables().get(var);
		
		return String.valueOf(value);
	}
	
	public Integer getExtendedStateVarAsInt(StateContext<?, ?> context, String var) {
		String value = this.getExtendedStateVarAsString(context, var);
		
		return StringUtils.isNumeric(value) ? NumberUtils.parseNumber(value,Integer.class) : null;
	}
	
	public void setExtendedStateVar(StateContext<?, ?> context, String var, Object value) {
		context.getExtendedState().getVariables().put(var, value);
	}
	
	public void setExtendedStateVarIfEmpty(StateContext<?, ?> context, String var, Object value) {
		Boolean exists = context.getExtendedState().getVariables().containsKey(var);
		
		if ( !exists ) {
			context.getExtendedState().getVariables().put(var, value);
		}
	}
}
