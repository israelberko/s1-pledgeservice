package org.ssm.demo.pledgeservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ssm.demo.pledgeservice.entity.Pledge;

@Repository
public interface PledgeRepository extends CrudRepository<Pledge, UUID>{
	 
}
