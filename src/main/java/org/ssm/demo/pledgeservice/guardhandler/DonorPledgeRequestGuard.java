package org.ssm.demo.pledgeservice.guardhandler;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;


public class DonorPledgeRequestGuard implements Guard<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestGuard.class);
	
	Utils utils;
	
	Predicate<Boolean> predicate;
	
	public DonorPledgeRequestGuard(Utils utils, Predicate<Boolean> predicate) {
		this.utils = utils;
		this.predicate = predicate;
	}

	@Override
	public boolean evaluate(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());

		Map<?,?> map = utils.getExtendedStateVar( context, "donor", Map.class );
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						utils.getAsInt(map, "amount"), 0);
		
		totalAmount += 
				ObjectUtils.defaultIfNull(
						utils.getExtendedStateVarAsInt(context, "totalAmount"), 0);
		
		Integer requestedAmount = 
				ObjectUtils.defaultIfNull( 
						utils.getExtendedStateVarAsInt(context, "requestedAmount"), 0);
		
		LOG.info("Comparing total amount ({}) to requested amount ({})...{}", 
				totalAmount, 
					requestedAmount,
						map);
		
		return predicate.test(totalAmount >= requestedAmount);
	}

}
