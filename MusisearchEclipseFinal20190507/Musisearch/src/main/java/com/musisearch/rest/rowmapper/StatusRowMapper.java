package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Status;



public class StatusRowMapper implements RowMapper<Status>{

	@Override
	public Status mapRow(ResultSet rs, int rowNum) throws SQLException {
		Status baseMap = (new BeanPropertyRowMapper<>(Status.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
