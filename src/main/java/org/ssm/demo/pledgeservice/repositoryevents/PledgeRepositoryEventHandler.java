package org.ssm.demo.pledgeservice.repositoryevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.PledgeSagaCoordinator;

@RepositoryEventHandler(Pledge.class) 
public class PledgeRepositoryEventHandler {
	@Autowired PledgeSagaCoordinator sagaCoordinator;
	Logger LOG = LoggerFactory.getLogger("Class AuthorEventHandler");
	    
    @HandleBeforeCreate
    public void handleAuthorBeforeCreate(Pledge pledge){
        LOG.info("Invoking before create....");
        sagaCoordinator.reset();
    }
}
