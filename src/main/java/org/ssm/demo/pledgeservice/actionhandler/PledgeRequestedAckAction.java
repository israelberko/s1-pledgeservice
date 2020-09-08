package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.descriptor.web.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;

@Component
public class PledgeRequestedAckAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedAckAction.class);
	@Autowired ApplicationEventPublisher publisher;
	@Autowired Utils utils;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("Invoking {}", this.getClass());
		
		Integer totalAmount = computeTotalPledge(context);
		
		utils.setExtendedStateVar(context, "totalAmount", totalAmount);
		
		LOG.info("Value of totalAmount: {}", totalAmount);

	}
	
	private Integer computeTotalPledge(StateContext<PledgeStates, PledgeEvents> context) {
		
		Map<?,?> pledgeMap  = utils.getExtendedStateVar(context, "pledge", Map.class);
		
		Map<?,?> donorMap   = utils.getExtendedStateVar(context, "donor", Map.class);
		
		Integer totalAmount = 
				ObjectUtils.defaultIfNull(
						utils.getAsInt(donorMap, "amount"), 0);
		
		totalAmount += 
				ObjectUtils.defaultIfNull(
						utils.getExtendedStateVarAsInt(context, "totalAmount"), 
							ObjectUtils.defaultIfNull(utils.getAsInt(pledgeMap, "actual_pledged_amount"), 0));
		
		return totalAmount;
	}
}
