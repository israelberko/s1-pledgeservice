package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

/**
 * 
 * state = PLEDGE_REQUESTED
 * status = PLEDGE_REQUESTED/PLEDGE_REQUESTED_PENDING
 *
 */
@Component
public class PledgeRequestedEntryAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedEntryAction.class);
	
	@Autowired PledgeService pledgeService;
	
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {

		LOG.info("---------- Invoking {} with {}", this.getClass(), context);

		pledgeService.findOne(context.getStateMachine().getUuid()).ifPresent(pledge -> {
			LOG.info("Pledge is {}",pledge);
			pledge.setState( PledgeStates.PLEDGE_REQUESTED.name() + "_PENDING" );
			pledgeService.updatePledge( pledge );
		});

		
	}
	
}
