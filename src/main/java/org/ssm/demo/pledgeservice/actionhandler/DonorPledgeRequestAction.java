package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.ContextService;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);
	
	@Autowired ApplicationEventPublisher publisher;

	@Autowired Utils utils;
	
	@Autowired ContextService contextService;
	
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
		this.initializeExtendedStateVars(context);
		
		Integer totalAmount = contextService.computeTotalPledge(context);
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount);
		
		LOG.info("Value of totalAmount:{}, event:{}",  totalAmount, context.getEvent());
		
		resendToDonor(context, totalAmount);
	}

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_REQUESTED.name())) {
			
			LOG.info("Sending to DonorService...{}", outbox);
			
			return outbox;
			
		} else {
			
			return null;
			
		}
	}
	
	private void resendToDonor(StateContext<PledgeStates, PledgeEvents> context, Integer totalAmount) {
	
		Pledge pledge = Pledge.of(utils.getExtendedStateVar(context, "pledge", Map.class));
		
		pledge.setActual_pledged_amount(totalAmount);
		
		pledgeService.savePledge( pledge );
	}
	
	
	private void initializeExtendedStateVars(StateContext<PledgeStates, PledgeEvents> context) {
		
		Map<?,?> map = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		utils.setExtendedStateVarIfEmpty(context, "requestedAmount", 
				ObjectUtils.defaultIfNull( utils.getAsInt(map, "requested_pledged_amount"), 0));
		
		utils.setExtendedStateVarIfEmpty(context, "totalAmount", 
				ObjectUtils.defaultIfNull( utils.getAsInt(map, "actual_pledged_amount"), 0));
		
	}
	
}
