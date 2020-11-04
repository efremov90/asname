package org.asname.dao.clients;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.clients.ClientDopoffice;

import java.sql.*;
import java.util.logging.Logger;

public class ClientDopofficeDAO {

    private Connection conn;
    private Logger logger = Logger.getLogger(ClientDopofficeDAO.class.getName());

    public ClientDopofficeDAO() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(ClientDopoffice client) throws Exception {
        logger.info("start");

        Integer result = null;
//        try {
        logger.info(client.toString());

        result = new ClientDAO().create(client);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        logger.info(":" + result);
        return result;
    }

    public boolean edit(ClientDopoffice client) throws SQLException {
        logger.info("start");

        boolean result = false;
//        try {
        new ClientDAO().edit(client);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        logger.info(":" + result);
        return result;
    }
}
