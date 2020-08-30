package org.ssm.demo.partyservice.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.ssm.demo.partyservice.entity.Party;

@Service
public class PartyCommandHandler {
Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	
	@KafkaListener(topics = "dbserver1.party.parties", groupId = "party-consumer")
	public Party suggested(Map<?,?> message) {
		Party party = Party.of(message);
		LOG.info(String.format("Message received: %s", party));
		return party;
	}
}
