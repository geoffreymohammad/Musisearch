package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Region;



public class RegionRowMapper implements RowMapper<Region>{

	@Override
	public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
		Region baseMap = (new BeanPropertyRowMapper<>(Region.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
