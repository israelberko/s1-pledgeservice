package org.ssm.demo.pledgeservice.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.repositories.PledgeRepository;

@Service
public class PledgeService {

    private static Logger LOG = LoggerFactory.getLogger(PledgeService.class);
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    PledgeRepository pledgeRepository;
    @Autowired
    PledgeOutboxService pledgeOutboxService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPledgeSaveAfter(Pledge pledge) {
        LOG.info("After ======== On Transactional Event Listener: Pledge: {}", pledge.toString());
        pledgeOutboxService.generatePledgeOutbox(pledge);
    }

    @Transactional
    public void updatePledge(Pledge pledge) {
        Optional<Pledge> optional = pledgeRepository.findById(pledge.getId());

        LOG.info("Invoking updatePledge with {}", pledge);
        LOG.info("Found on DB: {}", optional.orElse(null));

        optional.ifPresent(p -> {
            p.setActual_pledged_amount(pledge.getActual_pledged_amount());

            p.setStatus(pledge.getStatus());

            pledgeRepository.save(p);
        });
    }

    @Transactional
    public Pledge savePledge(Pledge pledge) {
        LOG.info("Invoking savePledge with {}", pledge);
        Pledge savePledge = pledgeRepository.save(pledge);
        applicationEventPublisher.publishEvent(savePledge);
        return savePledge;
    }

    public Optional<Pledge> findOne(UUID id){
        return pledgeRepository.findById(id);
    }
}
