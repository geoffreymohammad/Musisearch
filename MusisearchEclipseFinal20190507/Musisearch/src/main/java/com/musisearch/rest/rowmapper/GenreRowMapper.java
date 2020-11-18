package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;



public class GenreRowMapper implements RowMapper<Genre>{

	@Override
	public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
		Genre baseMap = (new BeanPropertyRowMapper<>(Genre.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
