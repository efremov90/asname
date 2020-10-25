package org.asname.service;

import org.asname.dao.ConfigureDAO;
import org.asname.dao.UserAccountDAO;
import org.asname.model.Configure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConfigureService {

    private static Logger logger = Logger.getLogger(ConfigureService.class.getName());
    private static Map<String, String> configures;

    public static String getConfigureValueByName(String name) throws SQLException {

        logger.info("start");
        if (configures == null) {
            ConfigureDAO configureDAO = new ConfigureDAO();
            ArrayList<Configure> ar = configureDAO.getConfigureAll();
            configures = configureDAO.getConfigureAll().stream()
                    .collect(Collectors.toMap(x -> x.getName(), x -> x.getValue()));
            logger.info(":" + configures.get(name));
            return configures.get(name);
        } else {
            logger.info(":" + configures.get(name));
            return configures.get(name);
        }
    }
}
