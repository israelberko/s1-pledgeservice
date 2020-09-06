package org.ssm.demo.pledgeservice.actionhandler;

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
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);
	@Autowired ApplicationEventPublisher publisher;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("In DonorPledgeRequestAction...");
		Map<?,?> currentPledge = context.getExtendedState().get("pledge", Map.class);
		context.getExtendedState().getVariables().putIfAbsent("requestedAmount", Integer.valueOf((String)currentPledge.get("requested_pledged_amount")));
		context.getExtendedState().getVariables().putIfAbsent("totalAmount", Integer.valueOf((String)currentPledge.get("actual_pledged_amount")));
		context.getMessage().getHeaders().put("pledgeId", currentPledge.get("id"));
		LOG.info("Value of requestedAmount:{}, totalAmount:{}",  currentPledge.get("requested_pledged_amount"), currentPledge.get("actual_pledged_amount"));
	}

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_REQUESTED.name())) {
			LOG.info("Sending to DonorService...{}", outbox);
			return outbox;
		} else {
			return null;
		}
	}
	
}
