package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    /**
     * Method to create a dataSource representing a stocks DB
     * Rge prefix spring.datasource will tell spring to pick 4 corresponding parameters from the application properties file
     * @return  DataSource
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }


    /**
     *Method to create a dataSource representing a stocks DB
     *Rge prefix spring.datasource-crudjpa will tell spring to pick 4 corresponding parameters from the application properties file
     *@return  DataSource
     */
    @Bean(name  ="dataSourceCurd")
    @ConfigurationProperties(prefix = "spring.datasource-crudjpa")
    public DataSource getDataSourceCurd(){
        return DataSourceBuilder.create().build();
    }


}
