package org.ssm.demo.partyservice.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssm.demo.partyservice.applicationevents.SendOutboxEvent;
import org.ssm.demo.partyservice.entity.Party;
import org.ssm.demo.partyservice.entity.PartyOutbox;
import org.ssm.demo.partyservice.repositories.PartyRepository;

@Service
public class PartyCommandHandler {
	static Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	
	@Autowired ApplicationEventPublisher applicationEventPublisher;
	@Autowired PartyRepository partyRepository;
	
	@Transactional
	@KafkaListener(topics = "dbserver1.party.party", groupId = "party-consumer")
	public Party proposed(Map<?,?> message, @Headers Map<?,?> headers) {
		Party party = Party.of(message);
		PartyOutbox partyOutbox = PartyOutbox.from(party);
		applicationEventPublisher.publishEvent(new SendOutboxEvent(partyOutbox));
		
		headers.forEach((key,value) -> LOG.info("Header {} {}",key,value));
		return party;
	}
}
