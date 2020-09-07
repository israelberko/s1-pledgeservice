package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.ContextService;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class DonorPledgeRequestEntryAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(DonorPledgeRequestEntryAction.class);
	
	@Autowired PledgeService pledgeService;
	
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		try {
		
		LOG.info("Invoking {}", this.getClass());
		
		Map<?,?> pledgeMap  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Pledge pledge = Pledge.of(pledgeMap);
		
		pledge.setState( PledgeStates.PLEDGE_REQUESTED.name() + "_PENDING" );
		
		pledgeService.savePledge( pledge );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
}
