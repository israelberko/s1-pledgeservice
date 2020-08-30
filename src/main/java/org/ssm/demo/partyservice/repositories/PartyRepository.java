package org.ssm.demo.partyservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ssm.demo.partyservice.entity.Party;

@Repository
public interface PartyRepository extends CrudRepository<Party, UUID>{
	 
}
