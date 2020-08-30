package org.ssm.demo.partyservice.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.ssm.demo.partyservice.entity.Party;

@Service
public class PartyCommandHandler {
Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	
	@KafkaListener(topics = "dbserver1.party.parties", groupId = "party-consumer")
	public Party suggested(Map<?,?> message, @Headers Map<?,?> headers) {
		Party party = Party.of(message);
		LOG.info(String.format("Message received: %s", party));
		headers.forEach((key,value) -> LOG.info(String.format("Header %s: %s",key,value)));
		return party;
	}
}
