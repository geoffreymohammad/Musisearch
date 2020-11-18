package com.musisearch.rest.repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Musisi;
import com.musisearch.rest.model.Region;
import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.rowmapper.AdminRowMapper;
import com.musisearch.rest.rowmapper.GenreRowMapper;
import com.musisearch.rest.rowmapper.MusisiRowMapper;
import com.musisearch.rest.rowmapper.RegionRowMapper;



@Repository
public class MusisiListRepo implements MusisiListInterface {

	@Autowired
	@Qualifier("ds1")
	private DataSource dataSource;
	@Autowired
	@Qualifier("ds1JdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;

	int getStartRow(int pageNumber, int ttlRowsPerPage) {
		int startRow = 0;
		if (pageNumber == 0)
			pageNumber = 1;
		startRow = ((pageNumber - 1) * ttlRowsPerPage) + 1;
		return startRow;
		
	}
	
	int getEndRow(int pageNumber, int ttlRowsPerPage) {
		int endRow = 0;
		if (pageNumber == 0)
			pageNumber = 1;
		endRow = pageNumber * ttlRowsPerPage;
		return endRow;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResultQuery getByPage(int pageNumber,  String inSearchValue, String inId, String inGender, String inGenre, String inRegion, String inInstrument1, String inInstrument2, String inInstrument3) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			
            Integer count = 0;
            
            Map<String, Object> result;
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                         .withProcedureName("getMusisiCount");
            
            SqlParameterSource param = new MapSqlParameterSource()
                        .addValue("inSearchValue", inSearchValue)
                        .addValue("inSearchValue", inSearchValue)
     					.addValue("inGender", inGender)
     					.addValue("inGenre", inGenre)
     					.addValue("inRegion", inRegion)
     					.addValue("inInstrument1", inInstrument1)
     					.addValue("inInstrument2", inInstrument2)
     					.addValue("inInstrument3", inInstrument3);
            
            result = simpleJdbcCall.execute(param);
            if(result!=null){
                  count = Integer.parseInt(result.get("TOTALROW").toString()); 
            }
			
			ArrayList<Object> lstMap = new ArrayList<>();
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getMusisiPage")
					.returningResultSet("RESULT_CURSOR", new MusisiRowMapper());
			
			param = new MapSqlParameterSource()
					.addValue("inSearchValue", inSearchValue)
					.addValue("inId", inId)
					.addValue("inGender", inGender)
					.addValue("inGenre", inGenre)
					.addValue("inRegion", inRegion)
					.addValue("inInstrument1", inInstrument1)
					.addValue("inInstrument2", inInstrument2)
					.addValue("inInstrument3", inInstrument3)
					.addValue("inStartRow", getStartRow(pageNumber, 10))
					.addValue("inEndRow", getEndRow(pageNumber, 10));
				
			result = simpleJdbcCall.execute(param);
			lstMap = (ArrayList) result.get("RESULT_CURSOR");
			double page = 0;
			if (lstMap != null && lstMap.size() > 0) {
				page = (Math.ceil((double)count/10));
				resultQuery.setIntegerResult((int)page);
				resultQuery.setLstObject(lstMap);
			} else {
				resultQuery.setErrorResult(true);
				resultQuery.setErrorMessage("Tidak ada data");
			}
		} catch (Exception e) {
			// log.error("Error RegionRepository - selectRegionMap : \n" + e.getMessage(),
			// e);
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}

		return resultQuery;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResultQuery getAll(String inSearchValue, String inId, String inGender, String inGenre, String inRegion, String inInstrument1, String inInstrument2, String inInstrument3, String likeBy) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			ArrayList<Object> lstMap = new ArrayList<>();
			Map<String, Object> result;
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getMusisiAll")
					.returningResultSet("RESULT_CURSOR", new MusisiRowMapper());

			SqlParameterSource param = new MapSqlParameterSource()
					.addValue("inSearchValue", inSearchValue)
					.addValue("inId", inId)
  					.addValue("inGender", inGender)
  					.addValue("inGenre", inGenre)
  					.addValue("inRegion", inRegion)
  					.addValue("inInstrument1", inInstrument1)
  					.addValue("inInstrument2", inInstrument2)
  					.addValue("inInstrument3", inInstrument3)
  					.addValue("likeBy", likeBy);

			result = simpleJdbcCall.execute(param);
			lstMap = (ArrayList) result.get("RESULT_CURSOR");
			double page = 0;
			if (lstMap != null && lstMap.size() > 0) {
				/*
				 * page = (Math.ceil((double)count/10));
				 * resultQuery.setIntegerResult((int)page);
				 */
				resultQuery.setObject(lstMap.get(0));
				resultQuery.setLstObject(lstMap);
			} else {
				resultQuery.setErrorResult(true);
				resultQuery.setErrorMessage("Tidak ada data");
			}
		} catch (Exception e) {
			// log.error("Error RegionRepository - selectRegionMap : \n" + e.getMessage(),
			// e);
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}

		return resultQuery;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultQuery getById(int idAdmin) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		

		return resultQuery;
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	public ResultQuery insertRegion(Musisi musisi) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
			if (musisi != null) {
				SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("insertMusisi")
						.returningResultSet("RESULT_CURSOR", new MusisiRowMapper());
				String mdy = dmyFormat.format(musisi.getTglLahir());
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", musisi.getNama())
				.addValue("inEmail", musisi.getEmail())
				.addValue("inTelp", musisi.getTelp())
				.addValue("inTglLahir",mdy)
				.addValue("inGender", musisi.getGender())
				.addValue("inGenre", musisi.getGenre())
				.addValue("inRegion", musisi.getRegion())
				.addValue("inInstrument1", musisi.getInstrument1())
				.addValue("inInstrument2", musisi.getInstrument2())
				.addValue("inInstrument3", musisi.getInstrument3());
				ArrayList<Object> lstMap = new ArrayList<>();
				Map<String, Object> result = simpleJdbcCall.execute(param);
				lstMap = (ArrayList) result.get("RESULT_CURSOR");
				double page = 0;
				if (lstMap != null && lstMap.size() > 0) {
					resultQuery.setObject(lstMap.get(0));
				} else {
					resultQuery.setErrorResult(true);
					resultQuery.setErrorMessage("Tidak ada data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultQuery updateRegion(int inGenre, int inRegion, int inInstrument1, int inInstrument2, int inInstrument3, String inBiography, String inImagePath, String inImageString, int inId) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("updateMusisi")
				.returningResultSet("RESULT_CURSOR", new MusisiRowMapper());
				
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inBiography", inBiography)
				.addValue("inGenre", inGenre)
				.addValue("inRegion", inRegion)
				.addValue("inInstrument1", inInstrument1)
				.addValue("inInstrument2", inInstrument2)
				.addValue("inInstrument3", inInstrument3)
				.addValue("inImagePath", inImagePath)
				.addValue("inId", inId)
				.addValue("inModifiedBy", inId);
				Map<String, Object> result = simpleJdbcCall.execute(param);
				ArrayList<Object> lstMap = new ArrayList<>();
				lstMap = (ArrayList) result.get("RESULT_CURSOR");
				double page = 0;
				if (lstMap != null && lstMap.size() > 0) {
					resultQuery.setObject(lstMap.get(0));
				} else {
					resultQuery.setErrorResult(true);
					resultQuery.setErrorMessage("Tidak ada data");
				}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}

	@Override
	public ResultQuery deleteRegion(Musisi musisi, Object created) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultQuery insertLike(String idMusisi, String idLike) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("insertLike")
						.returningResultSet("RESULT_CURSOR", new MusisiRowMapper());
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inMusisi", idMusisi)
				.addValue("inCreatedBy", idLike)
				.addValue("inModifiedBy", idLike);
				ArrayList<Object> lstMap = new ArrayList<>();
				Map<String, Object> result = simpleJdbcCall.execute(param);
				
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}

	
}
