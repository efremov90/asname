package org.asname.entity.clients;

import javax.persistence.*;

@Entity
@Table(name = "atms")
//@PrimaryKeyJoinColumn(referencedColumnName = "client_id")
public class ClientATM extends Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "client_id", nullable = false)
    private long ClientId;

    @Column(name = "atm_type", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private ATMTypeType AtmType;

    public ClientATM() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }


    public long getClientId() {
        return ClientId;
    }

    public void setClientId(long clientId) {
        ClientId = clientId;
    }

    public ATMTypeType getAtmType() {
        return AtmType;
    }

    public void setAtmType(ATMTypeType atmType) {
        AtmType = atmType;
    }
}
