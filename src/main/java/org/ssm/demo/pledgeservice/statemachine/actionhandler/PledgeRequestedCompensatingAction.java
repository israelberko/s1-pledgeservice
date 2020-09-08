package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;

@Service
public class PledgeRequestedCompensatingAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedCompensatingAction.class);
	
	@Autowired ApplicationEventPublisher publisher;

	@Autowired Utils utils;
	
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
		Map<?,?> pledgeMap = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Pledge pledge = Pledge.of(pledgeMap);
		
		if (!pledge.getState().equals(PledgeEvents.PLEDGE_CANCELLED_ACK.name())) {
		
			pledge.setState(PledgeStates.PLEDGE_CANCEL_REQUESTED.name());	
			
			pledgeService.savePledge(pledge);
			
		}
	}

//	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-cancel-consumer")
//	@SendTo("donor.cancel.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		
		LOG.info("Cancelling Pledge...");
		
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_CANCEL_REQUESTED.name())) {
			
			LOG.info("Sending cancellation to DonorService...{}", outbox);
			
			return outbox;
			
		} else {
			return null;
		}
	}
	
}
