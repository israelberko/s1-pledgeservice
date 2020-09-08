package org.ssm.demo.pledgeservice.service;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;

@Service
public class PledgeSagaCoordinator {
	Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);
	@Autowired StateMachine<PledgeStates,PledgeEvents> stateMachine;
	
	@SuppressWarnings("deprecation")
	public void handleTrigger(PledgeEvents dispatchEvent, Map<String,?> extendedState, UUID pledge_id) {
		LOG.info("Dispatching event {} to state machine from saga coordinator: {}, {}, complete:{}", 
				dispatchEvent, extendedState, 
					stateMachine.getExtendedState().getVariables().get("pledge"),
						stateMachine.isComplete());
		
		stateMachine.getExtendedState().getVariables().putAll(extendedState);
		
		stateMachine.sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.setHeader("pledge_id", pledge_id)
				.build());
	}
}
