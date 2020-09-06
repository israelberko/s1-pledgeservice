package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents> {

	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		//execute any additional synchronous operations here
	}

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDestination(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		LOG.info("About to send...{}", outbox);
		return outbox;
	}

}
