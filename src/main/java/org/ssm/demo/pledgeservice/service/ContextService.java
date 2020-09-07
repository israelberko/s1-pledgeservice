package org.ssm.demo.pledgeservice.service;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;
import org.springframework.stereotype.Service;

@Service
public class ContextService {
	
	Logger LOG = LoggerFactory.getLogger(ContextService.class);
	
	@Autowired Utils utils;
	
	public Integer computeTotalPledge(StateContext<PledgeStates, PledgeEvents> context) {
		
		Map<?,?> pledgeMap  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Map<?,?> donorMap   = utils.getExtendedStateVar(context, "donor", Map.class);
		
		
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						utils.getAsInt(donorMap, "amount"), 0);
		
		LOG.info("So the total is {}, context value of total is {}, and actual_pledge_amount is {}", 
				totalAmount, utils.getExtendedStateVarAsInt(context, "totalAmount"), utils.getAsInt(pledgeMap, "actual_pledged_amount"));
		
		totalAmount += 
				ObjectUtils.defaultIfNull(
						utils.getExtendedStateVarAsInt(context, "totalAmount"), 
							ObjectUtils.defaultIfNull(utils.getAsInt(pledgeMap, "actual_pledged_amount"), 0));
		
		return totalAmount;
	}
}
