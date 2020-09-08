package org.ssm.demo.pledgeservice.statemachine.actionhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;

@Service
public class PledgeRequestedCompensatingAction implements Action<PledgeStates, PledgeEvents>{
	
	Logger LOG = LoggerFactory.getLogger(PledgeRequestedCompensatingAction.class);

	@Override
	public void execute(StateContext<PledgeStates, PledgeEvents> context) {
		
	}
	
	public PledgeOutbox sendToDonor(Map<?,?> message) {
		
		return null;
		
	}
	
}
