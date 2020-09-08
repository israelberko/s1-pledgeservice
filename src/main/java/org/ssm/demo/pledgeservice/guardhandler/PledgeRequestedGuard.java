package org.ssm.demo.pledgeservice.guardhandler;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;


public class PledgeRequestedGuard implements Guard<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedGuard.class);
	
	Utils utils;
	
	Predicate<Boolean> predicate;
	
	public PledgeRequestedGuard(Utils utils, Predicate<Boolean> predicate) {
		this.utils = utils;
		this.predicate = predicate;
	}

	@Override
	public boolean evaluate(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		Map<?,?> donorMap = utils.getExtendedStateVar( context, "donor", Map.class );

		Map<?,?> pledgeMap = utils.getExtendedStateVar( context, "pledge", Map.class );
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						utils.getAsInt(donorMap, "amount"), 0);
		
		totalAmount += 
				ObjectUtils.defaultIfNull(
						utils.getExtendedStateVarAsInt(context, "totalAmount"), 
							ObjectUtils.defaultIfNull( utils.getAsInt(pledgeMap, "actual_pledged_amount"), 0));
		
		Integer requestedAmount = 
				ObjectUtils.defaultIfNull( 
					utils.getExtendedStateVarAsInt(context, "requestedAmount"), 
						ObjectUtils.defaultIfNull( utils.getAsInt(pledgeMap, "requested_pledged_amount"), 0));
		
		LOG.info("Comparing total amount ({}) to requested amount ({})...{}", 
				totalAmount, 
					requestedAmount,
						donorMap);
		
		return predicate.test(totalAmount >= requestedAmount);
	}

}
