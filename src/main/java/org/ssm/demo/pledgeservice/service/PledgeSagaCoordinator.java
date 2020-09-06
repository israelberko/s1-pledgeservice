package org.ssm.demo.pledgeservice.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class PledgeSagaCoordinator {
	Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);
	@Autowired StateMachine<PledgeStates,PledgeEvents> stateMachine;
	
	@SuppressWarnings("deprecation")
	public void handleTrigger(Map<String,?> extendedState, PledgeEvents dispatchEvent) {
		LOG.info("Dispatching event {} to state machine from saga coordinator: {}", dispatchEvent, extendedState);
		stateMachine.getExtendedState().getVariables().putAll(extendedState);
		stateMachine.sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.build());
	}
}
