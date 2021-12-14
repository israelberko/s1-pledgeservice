package org.ssm.demo.pledgeservice.statemachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PledgeStateChangeInterceptor  extends StateMachineInterceptorAdapter<PledgeStates, PledgeEvents> {

    @Override
    public void preStateChange(State<PledgeStates, PledgeEvents> state, Message<PledgeEvents> message,
                               Transition<PledgeStates, PledgeEvents> transition,
                               StateMachine<PledgeStates, PledgeEvents> stateMachine) {
        log.info(String.format("************** preStateChange ******************"));
        log.info(String.format("transition from: %s to: %s with event: %s", transition.getSource().getId() , transition.getTarget().getId(), transition.getTrigger().getEvent()));
        Optional.ofNullable(message).ifPresent(msg -> {
            log.info(String.format("************** preStateChange ******************"));
        });
    }

        @Override
    public StateContext<PledgeStates, PledgeEvents> preTransition(StateContext<PledgeStates, PledgeEvents> stateContext) {
        log.info(String.format("************** preTransition ******************"));
        log.info(String.format("************** CONTEXt ******************" + stateContext.toString()));
        return super.preTransition(stateContext);
    }

}
