package org.ssm.demo.pledgeservice.statemachinehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class PledgeSMActionHandler {
	Logger LOG = LoggerFactory.getLogger(PledgeSMActionHandler.class);
	@Autowired KafkaTemplate<String, PledgeOutbox> kafkaTemplate;
	
	@Bean public Action<PledgeStates,PledgeEvents> donorAction(){
		LOG.info("Sending from Action...");
		return context -> LOG.info(context.getMessage().toString());
	}

}
