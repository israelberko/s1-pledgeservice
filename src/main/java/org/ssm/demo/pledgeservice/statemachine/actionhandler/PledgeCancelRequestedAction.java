package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

import java.util.Map;

@Service
public class PledgeCancelRequestedAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeCancelRequestedAction.class);
	
	@Autowired ApplicationEventPublisher publisher;

	@Autowired Utils utils;
	
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
	}

//	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "cancel-pledge-consumer")
//	@SendTo("donor.cancel.inbox")
	public PledgeOutbox sendCancelRequestToDonor(Map<?,?> message) {
		// Unlike PledgeRequestedAction, this only expects a single response
		// i.e. it uses Asynchronous Request-Response pattern.
		PledgeOutbox outbox = PledgeOutbox.of(message);
		
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_CANCEL_REQUESTED.name())) { // only handle event_type = PLEDGE_CANCEL_REQUESTED
			
			LOG.info("\n\n===========================\nSending cancellation to DonorService...{}" +
						"\n===========================\n\n", outbox);
			
			return outbox;
			
		} else { 
			
			return null;
			
		}
	}
	
}
