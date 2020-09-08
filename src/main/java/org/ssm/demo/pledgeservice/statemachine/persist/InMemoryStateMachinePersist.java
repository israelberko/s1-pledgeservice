package org.ssm.demo.pledgeservice.statemachine.persist;

import java.util.HashMap;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;

public class InMemoryStateMachinePersist implements StateMachinePersist<PledgeStates, PledgeEvents, String> {
	private final HashMap<String, StateMachineContext<PledgeStates, PledgeEvents>> contexts = new HashMap<>();

	@Override
	public void write(StateMachineContext<PledgeStates, PledgeEvents> context, String contextObj) throws Exception {
		contexts.put(contextObj, context);
	}

	@Override
	public StateMachineContext<PledgeStates, PledgeEvents> read(String contextObj) throws Exception {
		return contexts.get(contextObj);
	}

}