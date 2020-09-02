package org.ssm.demo.pledgeservice.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class PledgeSagaCoordinator {
	@Autowired StateMachine<PledgeStates,PledgeEvents> stateMachine;
	
	@SuppressWarnings("deprecation")
	public void handleRequestPledge(UUID pledge_id) {
		stateMachine.sendEvent(MessageBuilder
				.withPayload(PledgeEvents.REQUEST_PLEDGE)
				.setHeader("pledge_id", pledge_id)
				.build());
	}
}
