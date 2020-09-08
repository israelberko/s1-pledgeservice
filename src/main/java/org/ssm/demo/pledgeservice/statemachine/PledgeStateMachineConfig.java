package org.ssm.demo.pledgeservice.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.ssm.demo.pledgeservice.actionhandler.PledgeRequestedCompensatingEntryAction;
import org.ssm.demo.pledgeservice.actionhandler.PledgeRequestedAckAction;
import org.ssm.demo.pledgeservice.actionhandler.PledgeRequestedAction;
import org.ssm.demo.pledgeservice.actionhandler.PledgeRequestedCompensatingAction;
import org.ssm.demo.pledgeservice.actionhandler.PledgeRequestedEntryAction;
import org.ssm.demo.pledgeservice.actionhandler.ErrorAction;
import org.ssm.demo.pledgeservice.actionhandler.PledgeMatchedEntryAction;
import org.ssm.demo.pledgeservice.guardhandler.PledgeRequestedGuard;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;

@Configuration
@EnableStateMachine
public class PledgeStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<PledgeStates, PledgeEvents> {
	Logger LOG = LoggerFactory.getLogger(PledgeStateMachineConfig.class);
	
	@Autowired PledgeRequestedAckAction requestAckAction;
	
	@Autowired PledgeRequestedAction requestAction;
	
	@Autowired PledgeRequestedEntryAction requestEntryAction;
	
	@Autowired PledgeRequestedCompensatingAction cancelAction;
	
	@Autowired PledgeMatchedEntryAction matchEntryAction;
	
	@Autowired PledgeRequestedCompensatingEntryAction cancelEntryAction;
	
	@Autowired ErrorAction errorAction;
	
	@Autowired Utils utils;

    @Override
    public void configure(StateMachineConfigurationConfigurer<PledgeStates, PledgeEvents> config)
            throws Exception {
        config
            .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<PledgeStates, PledgeEvents> states)
            throws Exception {
        states
            .withStates()
                .initial(PledgeStates.PLEDGE_REQUESTED)
                .end(PledgeStates.PLEDGE_MATCHED)
                    .state(PledgeStates.PLEDGE_REQUESTED, requestEntryAction, null)
                    .state(PledgeStates.PLEDGE_MATCHED, matchEntryAction, null)
                    .state(PledgeStates.PLEDGE_CANCELLED, cancelEntryAction, null);
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
                .action(requestAckAction, errorAction)
                .guard(new PledgeRequestedGuard(utils, mustPass -> mustPass))
                .and()
            .withExternal()
                .source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_REQUESTED)
                .event(PledgeEvents.PLEDGE_MATCHED)
                .action(requestAckAction, errorAction)
                .guard(new PledgeRequestedGuard(utils, mustPass -> !mustPass));
        
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