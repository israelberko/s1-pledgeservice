package org.ssm.demo.pledgeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;
import org.ssm.demo.pledgeservice.statemachine.service.PledgeStateMachineService;

import java.util.Map;
import java.util.UUID;

@Service
public class PledgeSagaCoordinator {
    @Autowired
    Utils utils;
    @Autowired
    PledgeStateMachineService stateMachineService;
//    @Autowired
//    StateMachine<PledgeStates, PledgeEvents> stateMachine;

    Logger LOG = LoggerFactory.getLogger(PledgeSagaCoordinator.class);

    @SuppressWarnings("deprecation")
    public void handleTrigger(PledgeEvents dispatchEvent, Map<String, ?> extendedState, UUID pledge_id) {

		StateMachine<PledgeStates, PledgeEvents> stateMachine = stateMachineService.getStateMachine(pledge_id);

		stateMachine.getExtendedState().getVariables().putAll(extendedState);

		stateMachine.getExtendedState().getVariables().put("pledge_id", pledge_id);

		stateMachine.start();

		LOG.info("\n\n===========================\n" +
				"Dispatching event {} to state machine from saga coordinator: {}" +
				"\n=================================\n\n",
					dispatchEvent, stateMachine.getExtendedState());

		stateMachine.sendEvent(MessageBuilder
				.withPayload(dispatchEvent)
				.setHeader("pledge_id", pledge_id)
				.build());
    }
}
