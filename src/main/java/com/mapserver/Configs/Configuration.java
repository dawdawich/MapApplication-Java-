package com.mapserver.Configs;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class Configuration {


    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/map_db");
        dataSource.setUsername("root");
        dataSource.setPassword("дщкввфцвф1");
        return dataSource;
    }

}
