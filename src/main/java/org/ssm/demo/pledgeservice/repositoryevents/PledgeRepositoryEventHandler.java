package org.ssm.demo.pledgeservice.repositoryevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.ssm.demo.pledgeservice.applicationevents.LoadStateMachineEvent;
import org.ssm.demo.pledgeservice.entity.Pledge;

@RepositoryEventHandler(Pledge.class) 
public class PledgeRepositoryEventHandler {
	@Autowired ApplicationEventPublisher applicationEventPublisher;
	Logger LOG = LoggerFactory.getLogger("Class AuthorEventHandler");
	    
    @HandleBeforeCreate
    public void handleBeforeCreate(Pledge pledge){
        LOG.info("Invoking after create....");
        applicationEventPublisher.publishEvent(new LoadStateMachineEvent(pledge));
    }
}

