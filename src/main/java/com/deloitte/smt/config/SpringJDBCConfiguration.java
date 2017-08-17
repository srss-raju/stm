package com.deloitte.smt.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.deloitte.smt.dao.PtDAO;
import com.deloitte.smt.dao.SmqDAO;
import com.deloitte.smt.dao.impl.PtDAOImpl;
import com.deloitte.smt.dao.impl.SmqDAOImpl;
 
@Configuration
public class SpringJDBCConfiguration {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/smtdev");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
 
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
 
    @Bean
    public SmqDAO smqDAO(){
    	SmqDAOImpl smqDao = new SmqDAOImpl();
    	smqDao.setJdbcTemplate(jdbcTemplate());
        return smqDao;
    }
    
    @Bean
    public PtDAO ptDAO(){
    	PtDAOImpl ptDao = new PtDAOImpl();
    	ptDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate());
        return ptDao;
    }
 
}