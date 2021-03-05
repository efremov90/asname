package org.asname.mvc.controllers;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
/*import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;*/
//import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/*@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"org.asname.entity.*","org.asname.service.*"})
@EnableJpaRepositories("org.asname.repository")*/
public class JpaConfig {


/*    private static final String PROP_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String PROP_DATABASE_USERNAME = "root";
    private static final String PROP_DATABASE_PASSWORD = "admin";
    private static final String PROP_DATABASE_URL = "jdbc:mysql://localhost:3306/crud?serverTimezone=Europe/Moscow";
    private static final String PROP_HIBERNATE_DIALECT = "org.hibernate.dialect.MySQL8Dialect";
    private static final String PROP_HIBERNATE_SHOW_SQL = "true";
    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "org.asname.entity.*";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(PROP_DATABASE_DRIVER);
        dataSource.setUrl(PROP_DATABASE_URL);
        dataSource.setUsername(PROP_DATABASE_USERNAME);
        dataSource.setPassword(PROP_DATABASE_PASSWORD);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN);
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", PROP_HIBERNATE_DIALECT);
        properties.put("hibernate.show_sql", PROP_HIBERNATE_SHOW_SQL);

        return properties;
    }*/
}
