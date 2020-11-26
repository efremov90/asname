package org.asname.repository;

import org.asname.entity.clients.ClientATM;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientATMRepository extends CrudRepository<ClientATM,Long> {

}
