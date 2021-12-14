package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.binders.PledgeConsumerBinder;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

import java.util.Map;

/**
 * 
 * state = PLEDGE_REQUESTED
 * status = PLEDGE_REQUESTED_PENDING
 *
 */
@Service
@RequiredArgsConstructor
public class PledgeRequestedAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedAction.class);
	
	@Autowired Utils utils;
	
	@Autowired PledgeService pledgeService;

	private final PledgeConsumerBinder pledgeConsumerBinder;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());

		resendPledgeRequest(context);
	}


//	@StreamListener(PledgeConsumerBinder.ORDER_IN)
//	public void consumer(@Payload PledgeOutbox outbox) {
//		LOG.info("---------- PledgeRequestedAction  outbox consumed -- PledgeRequestedAction --: {}", outbox);
//		if (outbox.getEvent_type().equals(PledgeStatuses.PLEDGE_REQUESTED_PENDING.name())) {
//
//			LOG.info("Sending to DonorService...{}", outbox);
//
////			return outbox;
//
//		} else {
//
////			return null;
//
//		}
//	}

//	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
//	@SendTo("donor.inbox")//TODO: send to donor serice
//	public PledgeOutbox sendPledgeRequestToDonor(Pledge pledge) {
//		PledgeOutbox outbox = PledgeOutbox.from(pledge);
//
//		if (outbox.getEvent_type().equals(PledgeStatuses.PLEDGE_REQUESTED_PENDING.name())) {
//
//			LOG.info("Sending to DonorService...{}", outbox);
//
//			return outbox;
//
//		} else {
//
//			return null;
//
//		}
//	}
	
	private void resendPledgeRequest(StateContext<PledgeStates, PledgeEvents> context) {
		Pledge pledge = Pledge.of(utils.getExtendedStateVar(context, "pledge", Map.class));
		LOG.info("---------- PledgeRequestedAction  -- PledgeRequestedAction -- resendPledgeRequest --: {}", pledge);
		pledgeService.updatePledge( pledge );
//		sendPledgeRequestToDonor(pledge);
	}
	
}
