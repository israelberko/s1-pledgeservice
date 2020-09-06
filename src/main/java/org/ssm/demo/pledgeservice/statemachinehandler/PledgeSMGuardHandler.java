package org.ssm.demo.pledgeservice.statemachinehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.guard.Guard;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

public class PledgeSMGuardHandler {
	Logger LOG = LoggerFactory.getLogger(PledgeSMGuardHandler.class);
	@Autowired ApplicationEventPublisher publisher;
	
	@Bean public Guard<PledgeStates,PledgeEvents> requestMatchGuard(){
		return context -> {
			
			return true;
		};
	}

}
