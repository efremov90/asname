package org.asname.repository;

import org.asname.entity.clients.ClientATM;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface ClientATMRepository<C> extends CrudRepository<ClientATM,Long> {

   ClientATM findByClientCode(String clientCode);

}
