package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

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
		
		Map<?,?> pledgeMap  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Pledge pledge = Pledge.of(pledgeMap);
		
		pledge.setState( PledgeStates.PLEDGE_MATCHED.name() );
		
		pledge.setActual_pledged_amount( utils.getPledgeTotalAmount(context, null));
		
		LOG.info("=================================\nState changed to {}: {}=======================\n", 
				PledgeStates.PLEDGE_MATCHED, pledge);
		
		pledgeService.savePledge( pledge );
		
	}
	
}
