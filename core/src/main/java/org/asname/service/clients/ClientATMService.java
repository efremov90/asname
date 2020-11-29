package org.asname.service.clients;

import org.asname.dao.clients.ClientATMDAO;
import org.asname.dao.users.UserAccountDAO;
import org.asname.db.connection.MySQLConnection;
import org.asname.audit.model.AuditOperType;
import org.asname.entity.clients.ClientTypeType;
import org.asname.model.clients.ATMTypeType;
import org.asname.model.clients.ClientATM;
import org.asname.model.users.UserAccount;
import org.asname.audit.service.AuditService;
import org.asname.repository.ClientATMRepository;
import org.asname.service.security.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.asname.model.security.Permissions.CLIENTS_CREATE;
import static org.asname.model.security.Permissions.CLIENTS_EDIT;

@Component
@Service
@ComponentScan("org.asname.repository")
public class ClientATMService {

    private Connection conn;
    private Logger logger = Logger.getLogger(ClientATMService.class.getName());


    @Autowired
    ClientATMRepository<org.asname.entity.clients.ClientATM> clientATMRepository;

    public ClientATMService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    @Transactional
    public Integer create(ClientATM client, int userAccountId) throws Exception {

        logger.info("start");

        Integer result = null;

//        try {
        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, CLIENTS_CREATE))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    CLIENTS_CREATE.name()));
        new ClientService().validateExistsClient(client.getClientCode());

//        conn.setAutoCommit(false);

        org.asname.entity.clients.ClientATM clientATM = new org.asname.entity.clients.ClientATM();

        clientATM.setClientCode(client.getClientCode());
        clientATM.setClientName(client.getClientName());
        clientATM.setAddress(client.getAddress());
        clientATM.setClientType(ClientTypeType.valueOf(client.getClientType().name()));
        clientATM.setCloseDate(client.getCloseDate());
        clientATM.setId(client.getId());
        clientATM.setAtmType(org.asname.entity.clients.ATMTypeType.valueOf(client.getAtmType().name()));

        clientATMRepository.save(clientATM);

/*        result = new ClientATMDAO().create(client);

        new AuditService().create(
                AuditOperType.CREATE_CLIENT,
                userAccountId,
                new java.util.Date(),
                String.format("Наименование клиента: %s \n" +
                                "Адрес: %s \n" +
                                "Тип банкомата: %s \n" +
                                "Дата закрытия: %s",
                        client.getClientName(),
                        client.getAddress(),
                        client.getAtmType().getDescrition(),
                        client.getCloseDate()),
                client.getClientCode()
        );*/

//        conn.commit();
//        conn.setAutoCommit(true);
/*        } catch (Exception e) {
            result=false;
            e.printStackTrace();
        }*/

        return result;
    }

    @Transactional
    public boolean edit(ClientATM client, int userAccountId) throws Exception {
        logger.info("start");

        boolean result = false;

//        try {
        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, CLIENTS_EDIT))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    CLIENTS_EDIT.name()));

        ClientATM currentClient = new ClientATMService().getClientByCode(client.getClientCode());

//        conn.setAutoCommit(false);

        result = new ClientATMDAO().edit(client);

        new AuditService().create(
                AuditOperType.EDIT_CLIENT,
                userAccountId,
                new java.util.Date(),
                String.format("Предыдущее состояние:\n" +
                                "Наименование клиента: %s \n" +
                                "Адрес: %s \n" +
                                "Тип банкомата: %s \n" +
                                "Дата закрытия: %s",
                        currentClient.getClientName(),
                        currentClient.getAddress(),
                        currentClient.getAtmType().getDescrition(),
                        currentClient.getCloseDate() != null ? currentClient.getCloseDate().toString() : "") + "\n" +
                        String.format("Новое состояние:\n" +
                                        "Наименование клиента: %s \n" +
                                        "Адрес: %s \n" +
                                        "Тип банкомата: %s \n" +
                                        "Дата закрытия: %s",
                                client.getClientName(),
                                client.getAddress(),
                                client.getAtmType().getDescrition(),
                                client.getCloseDate() != null ? client.getCloseDate().toString() : ""),
                client.getClientCode()
        );

//        conn.commit();
//        conn.setAutoCommit(true);
        /*} catch (Exception e) {
            result=false;
            e.printStackTrace();
        }*/

        return result;
    }

    public ClientATM getClientByCode(String clientCode) throws SQLException {

        logger.info("start");

        ClientATM client = null;
//        try {
        String sql = "SELECT " +
                "c.*, a.ATM_TYPE " +
                "FROM CLIENTS c " +
                "INNER JOIN ATMS a ON c.ID = a.CLIENT_ID " +
                "WHERE CLIENT_CODE = ? AND CLIENT_TYPE_ID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, clientCode);
        st.setInt(2, 1);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            client = new ClientATM();
            client.setId(rs.getInt("id"));
            client.setClientCode(rs.getNString("client_code"));
            client.setClientName(rs.getNString("client_name"));
            client.setAtmType(ATMTypeType.valueOf(rs.getNString("atm_type")));
            client.setAddress(rs.getNString("address"));
            client.setCloseDate(rs.getDate("close_date") != null ?
                    new java.util.Date(rs.getDate("close_date").getTime()) : null);
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return client;
    }
}
