package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.GroupBand;



public class GroupBandRowMapper implements RowMapper<GroupBand>{

	@Override
	public GroupBand mapRow(ResultSet rs, int rowNum) throws SQLException {
		GroupBand baseMap = (new BeanPropertyRowMapper<>(GroupBand.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
