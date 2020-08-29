package org.ssm.demo.partyservice.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.ssm.demo.partyservice.entity.Customer;

@Service
public class PartyCommandHandler {
Logger LOG = LoggerFactory.getLogger(PartyCommandHandler.class);
	
	@KafkaListener(topics = "dbserver1.inventory.customers", groupId = "sample-consumer")
	public Customer customerReceived(Map<?,?> message) {
		Customer customer = Customer.of(message);
		LOG.info(String.format("Message received: %s", customer));
		return customer;
	}
}
