package org.ssm.demo.pledgeservice.statemachine;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.ssm.demo.pledgeservice.statemachinehandler.PledgeSMActionHandler;

@Configuration
@EnableStateMachine
public class PledgeStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<PledgeStates, PledgeEvents> {
	@Autowired PledgeSMActionHandler actionHandler;
	@Autowired KafkaTemplate<Object,Object> kafkaTemplate;

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
                    .states(EnumSet.allOf(PledgeStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PledgeStates, PledgeEvents> transitions)
            throws Exception {
        transitions
            .withExternal()
                .source(PledgeStates.PLEDGE_REQUESTED).target(PledgeStates.PLEDGE_MATCHED)
                .event(PledgeEvents.PLEDGE_MATCHED)
                .action(actionHandler.sendDonorPledgeRequestAction())
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
                System.out.println("State changed to " + to.getId());
            }
        };
    }
}