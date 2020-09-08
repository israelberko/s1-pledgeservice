package org.ssm.demo.pledgeservice.actionhandler;

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
public class PledgeRequestedAckAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedAckAction.class);
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		Integer totalAmount = utils.getPledgeTotalAmount(context);
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount);
		
		LOG.info("Value of totalAmount: {}", totalAmount);

	}
}
