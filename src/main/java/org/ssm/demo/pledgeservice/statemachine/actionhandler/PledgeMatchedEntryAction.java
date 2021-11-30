package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.PledgeService;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;

@Component
public class PledgeMatchedEntryAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeMatchedEntryAction.class);
	
	@Autowired PledgeService pledgeService;
	
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
		LOG.info("Invoking {}", this.getClass());
		
		Pledge pledge = utils.readPledge(context);
		
		pledge.setStatus( PledgeStates.PLEDGE_MATCHED.name() );
		
		pledge.setActual_pledged_amount( utils.getExtendedStateVarAsInt(context, "totalAmount"));
		
		LOG.info("\n\n=================================\nPLEDGE HAS BEEN MATCHED: {}\n=======================\n\n", 
				pledge);
		
		pledgeService.updatePledge( pledge );
		
	}
	
}
