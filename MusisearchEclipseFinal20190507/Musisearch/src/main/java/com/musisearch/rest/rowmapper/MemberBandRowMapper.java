package com.musisearch.rest.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.MemberBand;



public class MemberBandRowMapper implements RowMapper<MemberBand>{

	@Override
	public MemberBand mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberBand baseMap = (new BeanPropertyRowMapper<>(MemberBand.class)).mapRow(rs, rowNum);
		return baseMap;
	}

}
