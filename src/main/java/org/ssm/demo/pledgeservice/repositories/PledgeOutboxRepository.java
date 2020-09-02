package org.ssm.demo.pledgeservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;

@Repository
public interface PledgeOutboxRepository extends CrudRepository<PledgeOutbox, Long>{
	 
}
