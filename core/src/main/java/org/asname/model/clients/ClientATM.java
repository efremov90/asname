package org.asname.model.clients;

import static org.asname.model.clients.ClientTypeType.SELFSERVICE;

public class ClientATM extends Client {

    private ATMTypeType atmType;

    public ClientATM() {
        setClientType(SELFSERVICE);
    }

    public ATMTypeType getAtmType() {
        return atmType;
    }

    public void setAtmType(ATMTypeType atmType) {
        this.atmType = atmType;
    }
}
