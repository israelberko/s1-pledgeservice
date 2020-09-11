package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;


@Component
public class PledgeCancelRequestedNackAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(PledgeCancelRequestedNackAction.class);

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		LOG.info("\n\n===========================\nCancel SUCCESSFUL: FALSE\n===========================\n");

	}
}
