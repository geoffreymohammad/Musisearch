package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Initial;



public class InitialRowMapper implements RowMapper<Initial>{

	@Override
	public Initial mapRow(ResultSet rs, int rowNum) throws SQLException {
		Initial baseMap = (new BeanPropertyRowMapper<>(Initial.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
