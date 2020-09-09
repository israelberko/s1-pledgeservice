package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;


@Component
public class PledgeCancelRequestedAckAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(PledgeCancelRequestedAckAction.class);
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		utils.setExtendedStateVar(context, "cancelRequestSuccess", Boolean.TRUE);
		
		LOG.info("\n\n===========================\nCancel SUCCESSFUL: TRUE\n===========================\n\n");

	}
}
