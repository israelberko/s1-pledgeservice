package org.ssm.demo.pledgeservice.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;

@Component
public class ErrorAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(ErrorAction.class);

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.error("Error occurred during execution of action: {}\n{}", 
				context.getException().getMessage(),
				StringUtils.arrayToDelimitedString(context.getException().getStackTrace(),"\n"));
	}

}
