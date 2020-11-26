package org.asname.entity.clients;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import static org.asname.entity.clients.ClientTypeType.DOPOFFICE;

public class ClientDopoffice extends Client {

    public ClientDopoffice() {
        setClientType(DOPOFFICE);
    }

}
