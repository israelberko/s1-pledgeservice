package org.ssm.demo.pledgeservice.statemachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.repositories.PledgeRepository;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;
import org.ssm.demo.pledgeservice.statemachine.PledgeStateChangeInterceptor;
import org.ssm.demo.pledgeservice.statemachine.PledgeStates;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class PledgeStateMachineService {

    @Autowired
    StateMachineFactory<PledgeStates, PledgeEvents> stateMachineFactory;
    private final PledgeStateChangeInterceptor pledgeStateChangeInterceptor;
    @Autowired
    PledgeRepository pledgeRepository;

    Map<UUID, StateMachine<PledgeStates, PledgeEvents>> stateMachineStore = new ConcurrentHashMap<>();

    Logger LOG = LoggerFactory.getLogger(PledgeStateMachineService.class);


    public StateMachine<PledgeStates, PledgeEvents> getStateMachine(UUID pledge_id) {

        StateMachine<PledgeStates, PledgeEvents> stateMachine =
                stateMachineStore.getOrDefault(
                        pledge_id,
                        build(pledge_id));

        stateMachineStore.put(pledge_id, stateMachine);

        return stateMachine;
    }

    private StateMachine<PledgeStates, PledgeEvents> build(UUID pledge_id) {
        return stateMachineFactory.getStateMachine(pledge_id);
    }
}