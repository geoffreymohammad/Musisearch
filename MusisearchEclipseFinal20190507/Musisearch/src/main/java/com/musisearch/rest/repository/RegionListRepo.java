package com.musisearch.rest.repository;

import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.musisearch.rest.model.Region;
import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.rowmapper.RegionRowMapper;



@Repository
public class RegionListRepo implements RegionListInterface {

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
	public ResultQuery getByPage(int pageNumber,  String inNama, Integer inId) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			
            Integer count = 0;
            
            Map<String, Object> result;
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                         .withProcedureName("getRegionCount");
            
            SqlParameterSource param = new MapSqlParameterSource()
                         .addValue("inNama", inNama)
                         .addValue("inId", inId);
            
            result = simpleJdbcCall.execute(param);
            if(result!=null){
                  count = Integer.parseInt(result.get("TOTALROW").toString()); 
            }
			
			ArrayList<Object> lstMap = new ArrayList<>();
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getRegionPage")
					.returningResultSet("RESULT_CURSOR", new RegionRowMapper());
			
			param = new MapSqlParameterSource()
					.addValue("inNama", inNama)
					.addValue("inId", inId)
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
	public ResultQuery getAll(String inNama, Integer inId) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			ArrayList<Object> lstMap = new ArrayList<>();
			Map<String, Object> result;
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getRegionAll")
					.returningResultSet("RESULT_CURSOR", new RegionRowMapper());

			SqlParameterSource param = new MapSqlParameterSource()
					.addValue("inNama", inNama)
					.addValue("inId", inId);

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

	@Override
	public ResultQuery insertRegion(Region region, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
			if (region != null) {
				
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("insertRegion");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", region.getNama())
				.addValue("inCreatedBy", created)
				.addValue("inModifiedBy", created);
				Map<String, Object> result = simpleJdbcCall.execute(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}

	@Override
	public ResultQuery updateRegion(String inName, int inId, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("updateRegion");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", inName)
				.addValue("inId", inId)
				.addValue("inModifiedBy", created);
				Map<String, Object> result = simpleJdbcCall.execute(param);
		
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}

	@Override
	public ResultQuery deleteRegion(Region region, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("deleteRegion");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inId", region.getId())
				.addValue("inModifiedBy", created);
				Map<String, Object> result = simpleJdbcCall.execute(param);
		
		} catch (Exception e) {
			e.printStackTrace();
			resultQuery.setErrorResult(true);
			resultQuery.setErrorMessage(e.getMessage());
		}
		return resultQuery;
	}

	
	
}
