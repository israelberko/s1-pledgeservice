package org.ssm.demo.pledgeservice.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

@Component
public class DonorPledgeComputeTotalAction implements Action<PledgeStates, PledgeEvents>{
	Logger LOG = LoggerFactory.getLogger(DonorPledgeComputeTotalAction.class);
	@Autowired ApplicationEventPublisher publisher;

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		LOG.info("In DonorPledgeComputeTotalAction...");
		Map<?,?> currentDonor = context.getExtendedState().get("donor", Map.class);
		Integer amount        = Integer.valueOf((String)currentDonor.get("amount"));
		Integer totalAmount   = Integer.valueOf(context.getExtendedState().get("totalAmount", String.class));
		context.getExtendedState().getVariables().put("totalAmount", totalAmount + amount);
		LOG.info("Value of requestedAmount:{}, totalAmount:{}",  
				context.getExtendedState().get("requestedAmount", String.class), 
				context.getExtendedState().get("totalAmount", String.class));
		
	}
}
