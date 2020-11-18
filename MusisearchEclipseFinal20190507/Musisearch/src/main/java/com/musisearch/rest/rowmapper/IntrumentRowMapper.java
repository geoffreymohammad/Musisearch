package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Intrument;




public class IntrumentRowMapper implements RowMapper<Intrument>{

	@Override
	public Intrument mapRow(ResultSet rs, int rowNum) throws SQLException {
		Intrument baseMap = (new BeanPropertyRowMapper<>(Intrument.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
