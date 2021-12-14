package org.ssm.demo.pledgeservice.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.*;
import org.ssm.demo.pledgeservice.statemachine.guardhandler.DonorPledgeRequestGuard;

@Configuration
@EnableStateMachineFactory
public class PledgeStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<PledgeStates, PledgeEvents> {
	Logger LOG = LoggerFactory.getLogger(PledgeStateMachineConfig.class);

	@Autowired
	DonorPledgeComputeTotalAction requestComputeAction;

	@Autowired
	DonorPledgeRequestAction requestAction;

	@Autowired
	DonorPledgeRequestEntryAction requestEntryAction;

	@Autowired
	MatchPledgeEntryAction matchEntryAction;

	@Autowired ErrorAction errorAction;

	@Autowired Utils utils;

    @Override
    public void configure(StateMachineConfigurationConfigurer<PledgeStates, PledgeEvents> config)
            throws Exception {
        config
            .withConfiguration()
                .autoStartup(false)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<PledgeStates, PledgeEvents> states)
            throws Exception {
		states
				.withStates()
				.initial(PledgeStates.PLEDGE_REQUESTED, requestEntryAction)
				.end(PledgeStates.PLEDGE_MATCHED)
				.state(PledgeStates.PLEDGE_REQUESTED)
				.state(PledgeStates.PLEDGE_MATCHED, matchEntryAction, null)
				.state(PledgeStates.PLEDGE_CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PledgeStates, PledgeEvents> transitions)
            throws Exception {
		transitions
				.withExternal()
				.source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_REQUESTED)
				.event(PledgeEvents.PLEDGE_REQUESTED)
				.action(requestAction, errorAction)
				.and()
				.withExternal()
				.source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_MATCHED)
				.event(PledgeEvents.PLEDGE_MATCHED)
				.action(requestComputeAction, errorAction)
				.guard(new DonorPledgeRequestGuard(utils, mustPass -> mustPass==true))
				.and()
				.withExternal()
				.source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_REQUESTED)
				.event(PledgeEvents.PLEDGE_MATCHED)
				.action(requestAction, errorAction)
				.guard(new DonorPledgeRequestGuard(utils, mustPass -> mustPass!=true))
				.and()
				.withExternal()
				.source(PledgeStates.PLEDGE_MATCHED).target(PledgeStates.PLEDGE_CANCELLED)
				.event(PledgeEvents.PLEDGE_CANCELLED)
				.and()
				.withExternal()
				.source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_CANCELLED)
				.event(PledgeEvents.PLEDGE_CANCELLED);
        
    }

    @Bean
    public StateMachineListener<PledgeStates, PledgeEvents> listener() {
        return new StateMachineListenerAdapter<PledgeStates, PledgeEvents>() {
            @Override
            public void stateChanged(State<PledgeStates, PledgeEvents> from, State<PledgeStates, PledgeEvents> to) {
            	LOG.info("State changed to " + to.getId());
            }
            
        };
    }
}