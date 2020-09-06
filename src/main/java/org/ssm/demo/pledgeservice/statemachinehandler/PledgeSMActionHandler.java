package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
			PledgeOutbox actionMessage =
					PledgeOutbox.builder()
								.payload((String)context.getMessageHeader("payload"))
								.event_id((UUID)context.getMessageHeader("pledge_id"))
								.event_type(PledgeEvents.PLEDGE_REQUESTED.name()).build();
			

			LOG.info("Sending from Action...{}", actionMessage);
			this.sendToDestination(actionMessage);
		};
	}
	

	@SendTo("donor.inbox")
	public PledgeOutbox sendToDestination(PledgeOutbox message) {
		LOG.info("About to send...{}", message);
		return message;
	}

}
