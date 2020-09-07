package org.ssm.demo.pledgeservice.repositoryeventhandler;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;

@RepositoryEventHandler(Pledge.class)
@Component
public class PledgeEventHandler {
	
	Logger LOG = LoggerFactory.getLogger(PledgeEventHandler.class);
	
	@Autowired ApplicationEventPublisher applicationEventPublisher;

	@HandleBeforeSave
	public void beforeSave(Pledge pledge) {
		PledgeOutbox outbox = PledgeOutbox.from(pledge);
		applicationEventPublisher.publishEvent(outbox);
	}
}
