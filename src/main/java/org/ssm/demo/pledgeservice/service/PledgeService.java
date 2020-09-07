package org.ssm.demo.pledgeservice.service;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.repositories.PledgeOutboxRepository;
import org.ssm.demo.pledgeservice.repositories.PledgeRepository;

@Service
public class PledgeService {
	
	private static Logger LOG = LoggerFactory.getLogger(PledgeService.class);
	@Autowired PledgeOutboxRepository pledgeOutboxRepository;
	@Autowired ApplicationEventPublisher applicationEventPublisher;
	@Autowired PledgeRepository pledgeRepository;
	
	@Transactional
	@KafkaListener(topics = "dbserver1.pledge.pledge", groupId = "pledge-consumer")
	public Pledge createPledgeOutbox(Map<?,?> message) {
		Pledge pledge = Pledge.of(message);
		
		PledgeOutbox pledgeOutbox = PledgeOutbox.from(pledge);
		
		LOG.info("But Pledge: {}\nAnd PledgeOutbox: {}", pledge, pledgeOutbox);
		
		applicationEventPublisher.publishEvent(pledgeOutbox);
		
		return pledge;
	}
//	@Bean
//	public Consumer<Map<?,?>> createPledgeOutbox() {
//		return message -> {
//			LOG.info("Pledge: {}", message);
//			Pledge pledge = Pledge.of(message);
//			PledgeOutbox pledgeOutbox = PledgeOutbox.from(pledge);
//			applicationEventPublisher.publishEvent(new SendOutboxEvent(pledgeOutbox));
//		};
//	}
	

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void acceptOutboxEvent(PledgeOutbox event){
		LOG.info("Outbox: {}", event);
		
		pledgeOutboxRepository.save(event);
		
		pledgeOutboxRepository.delete(event);
	}
	
//	@Transactional
//	@Bean
//	public Consumer<Map<?,?>> pledgeRequested() {
//		return message -> {
//			PledgeOutbox pledgeRequested = PledgeOutbox.of(message);
//			LOG.info("PledgeOutbox: {}", pledgeRequested);
//			sagaCoordinator.handleRequestPledge(pledgeRequested.getEvent_id());
//		
//		};
//	}
	
	@Transactional
	public void savePledge(Pledge pledge) {
		Optional<Pledge> optional = pledgeRepository.findById(pledge.getId());
		
		optional.ifPresent( p -> {
			p.setActual_pledged_amount(pledge.getActual_pledged_amount());
			pledgeRepository.save(p);
		});
	}
}
