package org.asname.entity.clients;

import javax.persistence.*;
import java.util.Date;

//@MappedSuperclass
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "client_code", length = 10, unique = true, nullable = false)
    private String ClientCode;

    @Column(name = "client_name", length = 50, nullable = false)
    private String ClientName;

    @Column(name = "client_type_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ClientTypeType clientType;

    @Column(name = "address")
    private String Address;

    @Temporal(TemporalType.DATE)
    @Column(name = "close_date")
    private Date CloseDate;

    public Client() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public ClientTypeType getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeType clientType) {
        this.clientType = clientType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Date getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(Date closeDate) {
        CloseDate = closeDate;
    }
}
