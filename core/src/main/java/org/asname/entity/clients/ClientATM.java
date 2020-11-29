package org.asname.entity.clients;

import javax.persistence.*;

@Entity
@Table(name = "atms")
//@PrimaryKeyJoinColumn(referencedColumnName = "client_id")
public class ClientATM extends Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "atm_type", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private ATMTypeType atmType;

    public ClientATM() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public ATMTypeType getAtmType() {
        return atmType;
    }

    public void setAtmType(ATMTypeType atmType) {
        this.atmType = atmType;
    }
}
