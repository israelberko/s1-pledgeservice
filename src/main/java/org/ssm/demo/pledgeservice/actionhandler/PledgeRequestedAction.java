package org.ssm.demo.pledgeservice.actionhandler;

import java.sql.Timestamp;
import java.time.Instant;
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
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;

import com.ibm.icu.util.Calendar;

@Service
public class PledgeRequestedAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedAction.class);
	
	@Autowired ApplicationEventPublisher publisher;

	@Autowired Utils utils;
	
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
		resendPledgeRequest(context);
	}

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_REQUESTED.name())) { // only handle event_type = PLEDGE_REQUESTED
			
			LOG.info("Sending to DonorService...{}", outbox);
			
			return outbox;
			
		} else { 
			
			return null;
			
		}
	}
	
	private void resendPledgeRequest(StateContext<PledgeStates, PledgeEvents> context) {
	
		Pledge pledge = Pledge.of(utils.getExtendedStateVar(context, "pledge", Map.class));
		
		pledge.setUpdated_at(new Timestamp(Instant.now().toEpochMilli()));
		
		pledgeService.savePledge( pledge );
	}
	
}
