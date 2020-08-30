package org.ssm.demo.partyservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ssm.demo.partyservice.entity.PartyOutbox;

@Repository
public interface PartyOutboxRepository extends CrudRepository<PartyOutbox, Long>{
	 
}
