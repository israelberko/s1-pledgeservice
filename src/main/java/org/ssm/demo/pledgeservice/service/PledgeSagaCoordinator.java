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
import org.ssm.demo.pledgeservice.shared.Utils;

@Service
public class PledgeSagaCoordinator {
	@Autowired StateMachine<PledgeStates,PledgeEvents> stateMachine;
	@Autowired Utils utils;
	Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);
	
	@SuppressWarnings("deprecation")
	public void handleTrigger(PledgeEvents dispatchEvent, Map<String,?> extendedState, UUID pledge_id) {
		
		UUID existingPledgeId = stateMachine.getExtendedState().get("pledge_id", UUID.class);
		
		LOG.info("Existing: {}, new: {}", existingPledgeId, pledge_id);
		
		if ( existingPledgeId != null && ! pledge_id.equals( existingPledgeId ) ){
			
			resetStateMachine();
			
		}
		
		LOG.info("\n\n===========================\n");
		
		LOG.info("Dispatching event {} to state machine from saga coordinator: {}, {}", 
				dispatchEvent, extendedState, 
					stateMachine.getExtendedState());
		
		LOG.info("\n=================================\n\n");
		
		stateMachine.getExtendedState().getVariables().putAll(extendedState);
		
		stateMachine.getExtendedState().getVariables().put("pledge_id", pledge_id);
		
		stateMachine.sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.setHeader("pledge_id", pledge_id)
				.build());
	}
	
	private void resetStateMachine() {
		
		stateMachine.stop();
		
		stateMachine.start();
		
	}
}
