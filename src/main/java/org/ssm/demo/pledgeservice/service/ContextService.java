package org.ssm.demo.pledgeservice.service;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;
import org.springframework.stereotype.Service;

@Service
public class ContextService {
	
	@Autowired Utils utils;
	
	public Integer computeTotalPledge(StateContext<PledgeStates, PledgeEvents> context) {
		
		Map<?,?> pledgeMap  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Map<?,?> donorMap   = utils.getExtendedStateVar(context, "donor", Map.class);
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						utils.getAsInt(donorMap, "amount"), 0);
		
		totalAmount += 
				ObjectUtils.defaultIfNull(
						utils.getExtendedStateVarAsInt(context, "totalAmount"), 
							utils.getAsInt(pledgeMap, "actual_pledge_amount"));
		
		return totalAmount;
	}
}
