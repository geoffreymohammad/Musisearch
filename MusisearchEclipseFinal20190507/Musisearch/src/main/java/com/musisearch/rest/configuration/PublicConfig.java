package com.musisearch.rest.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class PublicConfig {

	@Bean(name = "ds1")
    @Primary
    @ConfigurationProperties(prefix = "spring.ds_first")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "ds1JdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("ds1") DataSource dsOracle) {
		return new JdbcTemplate(dsOracle);
	}
	
}
