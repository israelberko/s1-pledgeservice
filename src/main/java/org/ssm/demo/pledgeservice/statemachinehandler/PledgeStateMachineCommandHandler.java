package org.ssm.demo.pledgeservice.statemachinehandler;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.binders.NewConsumerPledgeBinder;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeSagaCoordinator;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;

@Component
public class PledgeStateMachineCommandHandler {
    Logger LOG = LoggerFactory.getLogger(PledgeStateMachineCommandHandler.class);
    @Autowired
    PledgeSagaCoordinator coordinator;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @StreamListener(NewConsumerPledgeBinder.ORDER_IN)
    public void consumer(@Payload PledgeOutbox outbox) {
        LOG.info("---------- PledgeStateMachineCommandHandler outbox consumed: {}", outbox);
        applicationEventPublisher.publishEvent(outbox);
    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED'")
    public void handleInitializePledgeRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("---------- PledgeOutbox ---- PLEDGE_REQUESTED ---: {}", pledgeOutbox);

        coordinator.handleTrigger(PledgeEvents.PLEDGE_REQUESTED, ImmutableMap.of("pledge", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_PENDING'")
    public void handlePledgeRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox ---- PLEDGE_REQUESTED_PENDING ---: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_REQUESTED, ImmutableMap.of("pledge", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_NACK'")
    public void handleDonorNackRequest(PledgeOutbox pledgeOutbox) {

        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCELLED, ImmutableMap.of("donor",pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_ACK'")
    public void handleDonorAckRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox ---- PLEDGE_REQUESTED_ACK ---: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_MATCHED, ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());
    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_CANCEL_REQUESTED'")
    public void handleInitializeDonorCancelRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCEL_REQUESTED, ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_CANCEL_REQUESTED_PENDING'")
    public void handleDonorCancelRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCEL_REQUESTED, ImmutableMap.of("pledge", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_CANCEL_REQUESTED_ACK'")
    public void handleDonorCancelRequestAck(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCELLED,
                ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap(), "cancelRequestSuccess", Boolean.TRUE),
                pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_CANCEL_REQUESTED_NACK'")
    public void handleDonorCancelRequestNack(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCELLED,
                ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap(), "cancelRequestSuccess", Boolean.FALSE),
                pledgeOutbox.getEvent_id());

    }

}
