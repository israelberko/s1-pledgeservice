package org.ssm.demo.pledgeservice.guardhandler;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component 
public class DonorPledgeRequestGuard implements Guard<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestGuard.class);
	
	@Autowired Utils utils;

	@Override
	public boolean evaluate(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking DonorPledgeRequestGuard");
		
		Integer requestedAmount = ObjectUtils.defaultIfNull( 
				utils.getExtendedStateVarAsInt(context, "requestedAmount"), 0);
		
		Integer totalAmount     = ObjectUtils.defaultIfNull(
				utils.getExtendedStateVarAsInt(context, "totalAmount"), 0);
		
		return (totalAmount < requestedAmount);
	}

}
