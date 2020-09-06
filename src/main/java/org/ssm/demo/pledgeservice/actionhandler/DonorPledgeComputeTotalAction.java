package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class DonorPledgeComputeTotalAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(DonorPledgeComputeTotalAction.class);
	@Autowired ApplicationEventPublisher publisher;
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking DonorPledgeComputeTotalAction");
		
		Map<?, ?> currentDonor = utils.getExtendedStateVar(context, "donor", Map.class);
		
		Integer amount = utils.getAsInt(currentDonor, "amount");
		
		Integer totalAmount = utils.getExtendedStateVarAsInt(context, "totalAmount");
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount + amount);
		
		LOG.info("Value of totalAmount:{}", totalAmount);

	}
}
