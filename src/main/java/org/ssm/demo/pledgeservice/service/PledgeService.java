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
    @Autowired
    PledgeOutboxRepository pledgeOutboxRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    PledgeRepository pledgeRepository;
    @Autowired
    PledgeSagaCoordinator sagaCoordinator;

    @Transactional
    @KafkaListener(topics = "dbserver1.pledge.pledge", groupId = "pledge-consumer")
    public void onPledgeSave(Map<?, ?> message) {
        Pledge pledge = Pledge.of(message);

        createPledgeOutbox(pledge);
    }

    @Transactional
    public Pledge createPledgeOutbox(Pledge pledge) {
        PledgeOutbox pledgeOutbox = PledgeOutbox.from(pledge);

        LOG.info("On save: Pledge: {}\nAnd \nPledgeOutbox: {}", pledge, pledgeOutbox);

        acceptOutboxEvent(pledgeOutbox);

        return pledge;
    }


    //	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional
    public void acceptOutboxEvent(PledgeOutbox event) {
        LOG.info("Outbox: {}", event);

        pledgeOutboxRepository.save(event);

//        pledgeOutboxRepository.delete(event);
    }

    @Transactional
    public void updatePledge(Pledge pledge) {
        Optional<Pledge> optional = pledgeRepository.findById(pledge.getId());

        LOG.info("Invoking savePledge with {}", pledge);
        LOG.info("Found on DB: {}", optional.orElse(null));

        optional.ifPresent(p -> {
            p.setActual_pledged_amount(pledge.getActual_pledged_amount());

            p.setStatus(pledge.getStatus());

            pledgeRepository.save(p);
        });
    }

    @Transactional
    public void savePledge(Pledge pledge) {
        LOG.info("Invoking savePledge with {}", pledge);
        pledgeRepository.save(pledge);
    }
}
