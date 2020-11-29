package org.asname.entity.clients;

import javax.persistence.*;
import java.util.Date;

//@MappedSuperclass
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_code", length = 10, unique = true, nullable = false)
    protected String clientCode;

    @Column(name = "client_name", length = 50, nullable = false)
    private String clientName;

    @Column(name = "client_type_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ClientTypeType clientType;

    @Column(name = "address")
    private String address;

    @Temporal(TemporalType.DATE)
    @Column(name = "close_date")
    private Date closeDate;

    public Client() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ClientTypeType getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeType clientType) {
        this.clientType = clientType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
}
