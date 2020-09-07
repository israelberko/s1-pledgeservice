package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.ContextService;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class DonorPledgeComputeTotalAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(DonorPledgeComputeTotalAction.class);
	@Autowired ApplicationEventPublisher publisher;
	@Autowired Utils utils;
	@Autowired ContextService contextService;
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		Integer totalAmount = contextService.computeTotalPledge(context);
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount);
		
		LOG.info("Value of totalAmount: {}", totalAmount);
		
		Pledge pledge = Pledge.of(utils.getExtendedStateVar(context, "pledge", Map.class));
		
		pledge.setActual_pledged_amount(totalAmount);
		
		pledge.setState(PledgeStates.PLEDGE_MATCHED.name());
		
		pledgeService.savePledge( pledge );

	}
}
