package org.ssm.demo.pledgeservice.statemachine.guardhandler;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;


public class PledgeCancelRequestedGuard implements Guard<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeCancelRequestedGuard.class);
	
	Utils utils;
	
	Predicate<Boolean> predicate;
	
	public PledgeCancelRequestedGuard(Utils utils, Predicate<Boolean> predicate) {
		this.utils = utils;
		this.predicate = predicate;
	}

	@Override
	public boolean evaluate(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
		Boolean cancelRequestSuccess = utils.getExtendedStateVar(context, "cancelRequestSuccess", Boolean.class);
		
		LOG.info("Checking status of cancel request...{}", cancelRequestSuccess);
		
		return predicate.test(cancelRequestSuccess);
	}

}
