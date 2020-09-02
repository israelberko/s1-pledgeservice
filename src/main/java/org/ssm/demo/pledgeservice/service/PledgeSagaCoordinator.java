package org.ssm.demo.pledgeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class PledgeSagaCoordinator {
	Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);
	
	@Autowired StateMachine<PledgeStates,PledgeEvents> stateMachine;
	
	@SuppressWarnings("deprecation")
	public void handleRequest(PledgeOutbox pledgeEvent) {
		PledgeEvents dispatchEvent = PledgeEvents.valueOf(pledgeEvent.getEvent_type());
		LOG.info("Dispatching event {} to state machine from saga coordinator: {}", dispatchEvent, pledgeEvent);
		stateMachine.sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.setHeader("pledge_id", pledgeEvent.getEvent_id())
				.build());
	}
}
