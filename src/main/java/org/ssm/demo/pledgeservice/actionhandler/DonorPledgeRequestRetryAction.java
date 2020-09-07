package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Service
public class DonorPledgeRequestRetryAction extends DonorPledgeRequestAction{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestRetryAction.class);
	
	@Autowired PledgeService pledgeService;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		super.execute(context);
		
		Map<?,?> map = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		pledgeService.savePledge(Pledge.of(map));
	}

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-retry-consumer")
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		return super.sendToDonor(message);
	}
	
}
