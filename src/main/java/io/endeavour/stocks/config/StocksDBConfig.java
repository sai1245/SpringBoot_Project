package io.endeavour.stocks.config;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class StocksDBConfig {
    @Autowired
    DataSource dataSource;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedParamJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * To create an entity manager factory on the stocks DB, teh following steps need to be done:
     * 1)   Create an object of a class implementing EntityManagerFactory
     * 2)   Set the appropriate datasource to be tied to the EMF
     * 3)   Set the paclkages to scan for entities for this emf
     * 4)   create a vendor adapter class that defines who implement JPA spec (Hibernate in our case)
     * 5)   Setting the database type to connect to, for thge vendor adpater and set it into the EMF.
     * @return EntityManagerFactory
     */

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(){
        LocalContainerEntityManagerFactoryBean emf =new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("io.endeavour.stocks.entity.stocks");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setDatabase(Database.POSTGRESQL);


        emf.setJpaVendorAdapter(vendorAdapter);

        return emf;
    }

    @Bean( value = "transactionManager")
    public JpaTransactionManager getTransactionManager(@Qualifier(value ="entityManagerFactory") EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

}
