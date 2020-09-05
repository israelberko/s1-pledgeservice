package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);
	@Autowired KafkaTemplate<Object, Object> kafkaTemplate;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		PledgeOutbox actionMessage =
				PledgeOutbox.builder()
							.event_id((UUID)context.getMessageHeader("pledge_id"))
							.event_type(PledgeEvents.PLEDGE_REQUESTED.name()).build();
		
		LOG.info("Sending from Action...{}", actionMessage);
		
		kafkaTemplate.send("donor.inbox",actionMessage);
	}
}
