package org.ssm.demo.partyservice.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.ssm.demo.partyservice.applicationevents.SendOutboxEvent;
import org.ssm.demo.partyservice.entity.PartyOutbox;
import org.ssm.demo.partyservice.repositories.PartyOutboxRepository;

@Service
public class PartyOutboxService {
	
	private static Logger LOG = LoggerFactory.getLogger(PartyOutboxService.class);
	@Autowired PartyOutboxRepository partyOutboxRepository;
	
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void acceptOutboxEvent(SendOutboxEvent event){
		LOG.info("Outbox: {}", event.getPartyOutbox());
		partyOutboxRepository.save(event.getPartyOutbox());
		partyOutboxRepository.delete(event.getPartyOutbox());
	}
	
	@Transactional
	@KafkaListener(topics = "dbserver1.party.partiesoutbox", groupId = "partyoutbox-consumer")
	public PartyOutbox suggested(Map<?,?> message, @Headers Map<?,?> headers) {
		PartyOutbox partyOutbox = PartyOutbox.of(message);
		LOG.info("PartyOutbox: {}", partyOutbox);
		
		//TODO: Start StateMachine
		return partyOutbox;
	}
}
