package org.asname.dao;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.configure.Configure;

import java.sql.*;
import java.util.ArrayList;

public class ConfigureDAO {

    private Connection conn;

    public ConfigureDAO() {
        conn = MySQLConnection.getConnection();
    }

    public ArrayList<Configure> getConfigureAll() throws SQLException {
        ArrayList<Configure> configures = new ArrayList<>();
//        try {
        String sql = "SELECT " +
                "* " +
                "FROM CONFIGURE ";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Configure configure = new Configure();
            configure.setName(rs.getNString("Name"));
            configure.setValue(rs.getNString("Value"));
            configures.add(configure);
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return configures;
    }
}
