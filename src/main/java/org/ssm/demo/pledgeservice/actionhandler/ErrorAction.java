package org.ssm.demo.pledgeservice.actionhandler;

import org.slf4j.LoggerFactory;

import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class ErrorAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(ErrorAction.class);

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.error("Error occurred during execution of action: {}", 
				Arrays.toString(context.getException().getStackTrace()));
	}

}
