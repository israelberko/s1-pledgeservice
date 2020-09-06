package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.common.Utils;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);
	@Autowired ApplicationEventPublisher publisher;

	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking DonorPledgeRequestAction");
		
		Map<?,?> currentPledge  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Integer requestedAmount = utils.getAsInt(currentPledge, "requested_pledged_amount");
		
		Integer totalAmount     = utils.getAsInt(currentPledge, "actual_pledged_amount");
		
		utils.setExtendedStateVar(context, "requestedAmount", requestedAmount);
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount);
		
		LOG.info("Value of requestedAmount:{}, totalAmount:{}",  requestedAmount, totalAmount);
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
	
}
