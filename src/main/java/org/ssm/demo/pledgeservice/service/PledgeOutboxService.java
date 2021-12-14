package org.ssm.demo.pledgeservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.ssm.demo.pledgeservice.binders.NewConsumerPledgeBinder;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;
import org.ssm.demo.pledgeservice.repositories.PledgeOutboxRepository;

@Service
@RequiredArgsConstructor
public class PledgeOutboxService {

    private static Logger LOG = LoggerFactory.getLogger(PledgeOutboxService.class);
    @Autowired
    PledgeOutboxRepository pledgeOutboxRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    private final NewConsumerPledgeBinder newConsumerPledgeBinder;


    @Transactional
    public void generatePledgeOutbox(Pledge pledge) {
        PledgeOutbox pledgeOutbox = createPledgeOutbox(pledge);
        PledgeOutbox savedPledgeOutbox = savePledgeOutbox(pledgeOutbox);
        LOG.info("---------- After Saving PledgeOutbox for saving: {}", savedPledgeOutbox);
        newConsumerPledgeBinder.orderOut().send(MessageBuilder.withPayload(savedPledgeOutbox).build());
    }

    public PledgeOutbox createPledgeOutbox(Pledge pledge) {
        PledgeOutbox pledgeOutbox = PledgeOutbox.from(pledge);
        LOG.info("create Pledge Outbox PledgeOutbox: {}", pledgeOutbox);

        return pledgeOutbox;
    }

    @Transactional
    public PledgeOutbox savePledgeOutbox(PledgeOutbox pledgeOutbox) {
        LOG.info("Invoking savePledgeOutbox with {}", pledgeOutbox);
        return pledgeOutboxRepository.save(pledgeOutbox);
    }

    @Transactional
    public void deletePledgeOutbox(PledgeOutbox pledgeOutbox) {
        LOG.info("Invoking deletePledgeOutbox with {}", pledgeOutbox);
        pledgeOutboxRepository.delete(pledgeOutbox);

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPledgeOutboxSave(PledgeOutbox pledgeOutbox) {
        LOG.info("----------  On Transactional Event Listener: PledgeOutbox: {}", pledgeOutbox.toString());
        LOG.info("----------  Will be sent to Kafka/AMQ");
    }
}
