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
import org.ssm.demo.pledgeservice.actionhandler.CancelPledgeEntryAction;
import org.ssm.demo.pledgeservice.actionhandler.DonorPledgeComputeTotalAction;
import org.ssm.demo.pledgeservice.actionhandler.DonorPledgeRequestAction;
import org.ssm.demo.pledgeservice.actionhandler.DonorPledgeRequestCompensatingAction;
import org.ssm.demo.pledgeservice.actionhandler.DonorPledgeRequestEntryAction;
import org.ssm.demo.pledgeservice.actionhandler.ErrorAction;
import org.ssm.demo.pledgeservice.actionhandler.MatchPledgeEntryAction;
import org.ssm.demo.pledgeservice.guardhandler.DonorPledgeRequestGuard;
import org.ssm.demo.pledgeservice.shared.Utils;

@Configuration
@EnableStateMachine
public class PledgeStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<PledgeStates, PledgeEvents> {
	Logger LOG = LoggerFactory.getLogger(PledgeStateMachineConfig.class);
	
	@Autowired DonorPledgeComputeTotalAction requestComputeAction;
	
	@Autowired DonorPledgeRequestAction requestAction;
	
	@Autowired DonorPledgeRequestCompensatingAction cancelAction;
	
	@Autowired DonorPledgeRequestEntryAction requestEntryAction;
	
	@Autowired MatchPledgeEntryAction matchEntryAction;
	
	@Autowired CancelPledgeEntryAction cancelEntryAction;
	
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
            	.action(cancelAction, errorAction)
            	.and()
            .withExternal()
            	.source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_CANCELLED)
            	.event(PledgeEvents.PLEDGE_CANCELLED)
            	.and()
	        .withInternal()
				.source(PledgeStates.PLEDGE_MATCHED)
				.action(cancelAction)
				.timerOnce(15000);
        
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