package org.ssm.demo.pledgeservice.service;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.statemachine.persist.InMemoryStateMachinePersist;

@Service
public class PledgeSagaCoordinator {
	@Autowired StateMachineFactory<PledgeStates,PledgeEvents> stateMachineFactory;
	@Autowired InMemoryStateMachinePersist stateMachinePersist;
	StateMachinePersister<PledgeStates, PledgeEvents, String> persister = new DefaultStateMachinePersister<>(stateMachinePersist);
	Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);
	
	@SuppressWarnings("deprecation")
	public void handleTrigger(PledgeEvents dispatchEvent, Map<String,?> extendedState, UUID pledge_id) {
		
		LOG.info("\n\n===========================\n");
		
		LOG.info("Dispatching event {} to state machine from saga coordinator: {}, {}", 
				dispatchEvent, extendedState, 
					loadStateMachine(pledge_id).getExtendedState());
		
		LOG.info("\n=================================\n\n");
		
		StateMachine<PledgeStates,PledgeEvents> stateMachine = loadStateMachine(pledge_id);
		
		loadStateMachine(pledge_id).getExtendedState().getVariables().putAll(extendedState);
		
		loadStateMachine(pledge_id).sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.setHeader("pledge_id", pledge_id)
				.build());
		
		saveStateMachine(stateMachine, pledge_id);
	}
	
	private StateMachine<PledgeStates,PledgeEvents>  loadStateMachine(UUID pledge_id) {

		StateMachine<PledgeStates,PledgeEvents> stateMachine = stateMachineFactory.getStateMachine();;
		try {
			
			persister.restore(stateMachine, pledge_id.toString());
			
		} catch (Exception ex) {
			
			// NO-OP
			
		}
		return stateMachine;
	}
	
	private void saveStateMachine(StateMachine<PledgeStates,PledgeEvents> stateMachine, UUID pledge_id) {
		try {
			
			persister.persist(stateMachine, pledge_id.toString());
			
		} catch( Exception ex ) {
			
			// NO-OP
			
		}
	}
}
