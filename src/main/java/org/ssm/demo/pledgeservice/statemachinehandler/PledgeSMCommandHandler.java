package org.ssm.demo.pledgeservice.statemachinehandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.applicationevents.PledgeRoutedEvent;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.service.PledgeSagaCoordinator;
import org.ssm.demo.pledgeservice.statemachine.PledgeEvents;

@Component
public class PledgeSMCommandHandler {
	Logger LOG = LoggerFactory.getLogger(PledgeSMCommandHandler.class);
	@Autowired PledgeSagaCoordinator coordinator;
	@Autowired ApplicationEventPublisher applicationEventPublisher;
	

	@KafkaListener(topics = "dbserver1.pledge.pledge_outbox", groupId = "pledge-consumer")
	public void pledgeRequested(Map<?,?> message) {
		PledgeOutbox response = PledgeOutbox.of(message);
		LOG.info("PledgeOutbox: {}", response);
		applicationEventPublisher.publishEvent(new PledgeRoutedEvent(response));
	}
	
	@EventListener(condition = "event.event_type=='PLEDGE_REQUESTED'")
	public void handlePledgeRequest(PledgeOutbox pledgeEvent) {
		coordinator.handleRequest(pledgeEvent, PledgeEvents.PLEDGE_REQUESTED);
	}
	
	@EventListener(condition = "event.event_type=='PLEDGE_REQUESTED_ACK'")
	public void handleDonorAckRequest(PledgeOutbox pledgeEvent) {
		coordinator.handleRequest(pledgeEvent, PledgeEvents.PLEDGE_MATCHED);
	}
	
	@EventListener(condition = "event.event_type=='PLEDGE_REQUESTED_NACK'")
	public void handleDonorNackRequest(PledgeOutbox pledgeEvent) {
		coordinator.handleRequest(pledgeEvent, PledgeEvents.PLEDGE_CANCELLED);
	}

}
