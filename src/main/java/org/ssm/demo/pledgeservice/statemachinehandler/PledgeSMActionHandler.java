package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
@Configuration
public class PledgeSMActionHandler {
	Logger LOG = LoggerFactory.getLogger(PledgeSMActionHandler.class);
	@Autowired ApplicationEventPublisher publisher;
	
	@Bean public Action<PledgeStates,PledgeEvents> sendDonorPledgeRequestAction(){
		return context -> {
			// execute any synchronous actions here
			LOG.info("This context has some data...{}", ((Map<?,?>)context.getExtendedState().getVariables().get("pledge")).getClass());
			Map<?,?> currentPledge = context.getExtendedState().get("pledge", Map.class);
			context.getExtendedState().getVariables().put("requestedAmount", currentPledge.get("requested_pledged_amount"));
			context.getExtendedState().getVariables().put("totalAmount", currentPledge.get("actual_pledged_amount"));
			LOG.info("Also this has data...{}, {}, {}", 
					currentPledge.get("requested_pledged_amount"), 
					currentPledge.get("actual_pledged_amount"), 
					currentPledge);
			
		};
	}
	

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		PledgeOutbox outbox = PledgeOutbox.of(message);
		if (outbox.getEvent_type().equals(PledgeEvents.PLEDGE_REQUESTED.name())) {
			LOG.info("About to send to DonorService...{}", outbox);
			return outbox;
		} else {
			return null;
		}
	}

}
