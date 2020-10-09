package org.asname.model;

import java.util.Date;

import static org.asname.model.ClientTypeType.SELFSERVICE;

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
