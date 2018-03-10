package com.deloitte.smt.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dao.PtDAO;
import com.deloitte.smt.dao.SmqDAO;
import com.deloitte.smt.dao.SmtDAO;
import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dao.impl.ProductMedraHierarchyDAOImpl;
import com.deloitte.smt.dao.impl.PtDAOImpl;
import com.deloitte.smt.dao.impl.SmqDAOImpl;
import com.deloitte.smt.dao.impl.SmtDAOImpl;
import com.deloitte.smt.dao.impl.SocMedraHierarchyDAOImpl;
 
@Configuration
public class SpringJDBCConfiguration {
	
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource primaryDataSource() {
	    return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix="spring.fizer")
	public DataSource secondaryDataSource() {
	    return DataSourceBuilder.create().build();
	}
	
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(secondaryDataSource());
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(secondaryDataSource());
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
    
    @Bean
    public SocMedraHierarchyDAO socMedraHierarchyDAOImpl(){
    	SocMedraHierarchyDAOImpl socDto = new SocMedraHierarchyDAOImpl();
    	socDto.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate());
        return socDto;
    }
    
    @Bean
    public ProductMedraHierarchyDAO productMedraHierarchyDAOImpl(){
    	ProductMedraHierarchyDAOImpl productDto = new ProductMedraHierarchyDAOImpl();
    	productDto.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate());
        return productDto;
    }
    
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate() {
        return new JdbcTemplate(primaryDataSource());
    }
    
    @Bean
    public SmtDAO smtDAOImpl(){
        return new SmtDAOImpl();
    }
 
}