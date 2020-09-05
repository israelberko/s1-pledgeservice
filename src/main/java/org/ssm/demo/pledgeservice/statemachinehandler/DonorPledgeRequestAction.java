package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

public class DonorPledgeRequestAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestAction.class);
	KafkaTemplate<Object, Object> kafkaTemplate;
	
	public DonorPledgeRequestAction(KafkaTemplate<Object,Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

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
