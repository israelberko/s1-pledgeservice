package org.ssm.demo.pledgeservice.guardhandler;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.ssm.demo.pledgeservice.common.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

public class DonorPledgeRequestGuard implements Guard<PledgeStates, PledgeEvents>{
	
	@Autowired Utils utils;

	@Override
	public boolean evaluate(StateContext<PledgeStates, PledgeEvents> context) {
		Integer requestedAmount = ObjectUtils.defaultIfNull( 
				utils.getExtendedStateVarAsInt(context, "requestedAmount"), 0);
		
		Integer totalAmount     = ObjectUtils.defaultIfNull(
				utils.getExtendedStateVarAsInt(context, "totalAmount"), 0);
		
		return (totalAmount >= requestedAmount);
	}

}
