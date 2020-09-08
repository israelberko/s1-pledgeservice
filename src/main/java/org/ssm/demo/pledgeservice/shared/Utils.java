package org.ssm.demo.pledgeservice.shared;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;


@Component
public class Utils {
	public String getAsString(Map<?,?> map, String key) {
		if (map==null) return null;
		
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
		if (value != null) {
			context.getExtendedState().getVariables().put(var, value);
		}
	}
	
	public void initializeExtendedStateVar(StateContext<?, ?> context, String var, Object value) {
		Boolean exists = context.getExtendedState().getVariables().containsKey(var);
		
		if ( !exists && value != null) {
			context.getExtendedState().getVariables().put(var, value);
		}
	}
	
	public Integer getPledgeTotalAmount(StateContext<?, ?> context) {
		
		Map<?,?> donorMap = this.getExtendedStateVar( context, "donor", Map.class );

		Map<?,?> pledgeMap = this.getExtendedStateVar( context, "pledge", Map.class );
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						this.getAsInt(donorMap, "amount"), 0);
		
		totalAmount +=
				ObjectUtils.defaultIfNull(
						getExtendedStateVarAsInt(context, "totalAmount"), 
							ObjectUtils.defaultIfNull( getAsInt(pledgeMap, "actual_pledged_amount"), 0));
		
		return totalAmount;
	}
	
	public Integer getPledgeRequestedAmount(StateContext<?, ?> context) {

		Map<?,?> pledgeMap = this.getExtendedStateVar( context, "pledge", Map.class );
		
		Integer requestedAmount = 
				ObjectUtils.defaultIfNull( 
					this.getExtendedStateVarAsInt(context, "requestedAmount"), 
						ObjectUtils.defaultIfNull( this.getAsInt(pledgeMap, "requested_pledged_amount"), 0));
		
		return requestedAmount;
	}
}
