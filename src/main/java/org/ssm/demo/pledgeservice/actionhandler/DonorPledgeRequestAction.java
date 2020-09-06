package org.ssm.demo.pledgeservice.actionhandler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.repositories.PledgeRepository;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
@EnableKafka
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents> {

	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);

	@Override
	@SendTo("donor.inbox")
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		PledgeOutbox actionMessage =
						PledgeOutbox.builder()
									.payload((String)context.getMessageHeader("payload"))
									.event_id((UUID)context.getMessageHeader("pledge_id"))
									.event_type(PledgeEvents.PLEDGE_REQUESTED.name()).build();
				

		LOG.info("Sending from Action...{}", actionMessage);
		LOG.info("About to send...{}", actionMessage);
		this.sendToDestination(actionMessage);
	}

	@SendTo("donor.inbox")
	public PledgeOutbox sendToDestination(PledgeOutbox message) {
		LOG.info("About to send...{}", message);
		return message;
	}

}
