package org.ssm.demo.pledgeservice.statemachine.guardhandler;

import java.util.function.Predicate;

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
		
		Integer totalAmount = utils.getPledgeTotalAmount(context); 
		
		Integer requestedAmount = utils.getPledgeRequestedAmount(context);
		
		LOG.info("Comparing total amount ({}) to requested amount ({})...", 
				totalAmount, 
					requestedAmount);
		
		return predicate.test(totalAmount >= requestedAmount);
	}

}
