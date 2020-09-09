package org.ssm.demo.pledgeservice.statemachine.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;

@Component
public class PledgeStateMachineService {
	
	@Autowired StateMachineFactory<PledgeStates, PledgeEvents> stateMachineFactory;
	
	Map<UUID,StateMachine<PledgeStates, PledgeEvents>> stateMachineStore = new ConcurrentHashMap<>();
	
	Logger LOG = LoggerFactory.getLogger(PledgeStateMachineService.class);
	
	public StateMachine<PledgeStates, PledgeEvents> getStateMachine(UUID pledge_id) {
		
//		StateMachine<PledgeStates, PledgeEvents> stateMachine =
//				stateMachineStore.getOrDefault(
//						pledge_id,
//							stateMachineFactory.getStateMachine(pledge_id));
//		
//		stateMachineStore.put( pledge_id, stateMachine );
//		
//		LOG.info("State machine is {}", stateMachine );
		
		return stateMachineFactory.getStateMachine();
	}
}
