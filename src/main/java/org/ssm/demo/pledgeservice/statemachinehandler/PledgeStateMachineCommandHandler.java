package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeSagaCoordinator;
import org.ssm.demo.pledgeservice.shared.PledgeEvents;

import com.google.common.collect.ImmutableMap;

@Component
public class PledgeStateMachineCommandHandler {
    Logger LOG = LoggerFactory.getLogger(PledgeStateMachineCommandHandler.class);
    @Autowired
    PledgeSagaCoordinator coordinator;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "new-pledge-consumer")
    public void pledgeRequested(Map<?, ?> message) {
        LOG.info("pledgeRequested: {}", message);
        PledgeOutbox response = PledgeOutbox.of((Map<?, ?>) message.get("payload"));

        LOG.info("PledgeOutbox: {}", response);

        applicationEventPublisher.publishEvent(response);
    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED'")
    public void handleInitializePledgeRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        LOG.info("pledgeOutbox.getPayloadAsMap(): {}", pledgeOutbox.getPayloadAsMap());
        LOG.info("pledgeOutbox.getEvent_id(): {}", pledgeOutbox.getEvent_id());

        coordinator.handleTrigger(PledgeEvents.PLEDGE_REQUESTED, ImmutableMap.of("pledge", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_PENDING'")
    public void handlePledgeRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_REQUESTED, ImmutableMap.of("pledge", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_ACK'")
    public void handleDonorAckRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_MATCHED, ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

    }

    @EventListener(condition = "#pledgeOutbox.event_type eq 'PLEDGE_REQUESTED_NACK'") //TODO: implement
    public void handleDonorNackRequest(PledgeOutbox pledgeOutbox) {
        LOG.info("PledgeOutbox: {}", pledgeOutbox);
        coordinator.handleTrigger(PledgeEvents.PLEDGE_CANCEL_REQUESTED, ImmutableMap.of("donor", pledgeOutbox.getPayloadAsMap()), pledgeOutbox.getEvent_id());

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
