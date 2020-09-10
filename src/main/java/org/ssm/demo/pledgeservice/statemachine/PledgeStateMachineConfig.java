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
import org.springframework.statemachine.config.configurers.StateConfigurer.History;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;
import org.ssm.demo.pledgeservice.shared.PledgeStates;
import org.ssm.demo.pledgeservice.shared.Utils;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.ErrorAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeCancelRequestedAckAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeCancelRequestedAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeCancelRequestedEntryAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeCancelRequestedNackAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeMatchedEntryAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeRequestedAckAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeRequestedAction;
import org.ssm.demo.pledgeservice.statemachine.actionhandler.PledgeRequestedEntryAction;
import org.ssm.demo.pledgeservice.statemachine.guardhandler.PledgeRequestedGuard;

@Configuration
@EnableStateMachineFactory
public class PledgeStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<PledgeStates, PledgeEvents> {
	Logger LOG = LoggerFactory.getLogger(PledgeStateMachineConfig.class);
	
	@Autowired PledgeRequestedAction requestAction;
	
	@Autowired PledgeRequestedAckAction requestAckAction;
	
	@Autowired PledgeRequestedEntryAction requestEntryAction;
	
	@Autowired PledgeMatchedEntryAction matchEntryAction;
	
	@Autowired PledgeCancelRequestedAction cancelRequestAction;
	
	@Autowired PledgeCancelRequestedAckAction cancelRequestAckAction;
	
	@Autowired PledgeCancelRequestedNackAction cancelRequestNackAction;
	
	@Autowired PledgeCancelRequestedEntryAction cancelEntryAction;
	
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
                .initial(PledgeStates.IN_PROGRESS)
                .state(PledgeStates.SUSPENDED)
                .state(PledgeStates.COMPLETED)
                .and()
                .withStates()
                	.parent(PledgeStates.IN_PROGRESS)
                		.initial(PledgeStates.PLEDGE_REQUESTED)
	                    .state(PledgeStates.PLEDGE_REQUESTED, requestEntryAction, null)
	                    .state(PledgeStates.PLEDGE_CANCELLED, cancelEntryAction, null)
	                    .history(PledgeStates.PLEDGE_HISTORY, History.SHALLOW)
        			.parent(PledgeStates.COMPLETED)
            			.initial(PledgeStates.PLEDGE_MATCHED)
                    	.state(PledgeStates.PLEDGE_MATCHED, matchEntryAction, null);
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
                .guard(new PledgeRequestedGuard(utils, mustPass -> !mustPass))
                .and()
            .withExternal()
        		.source(PledgeStates.IN_PROGRESS).target(PledgeStates.PLEDGE_HISTORY)
        		.event(PledgeEvents.PLEDGE_CANCEL_REQUESTED);
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