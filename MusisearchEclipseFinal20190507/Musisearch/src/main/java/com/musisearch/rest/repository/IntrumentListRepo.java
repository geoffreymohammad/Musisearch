package com.musisearch.rest.repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import com.musisearch.rest.model.Intrument;
import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.rowmapper.IntrumentRowMapper;



@Repository
public class IntrumentListRepo implements IntrumentListInterface {

	@Autowired
	@Qualifier("ds1")
	private DataSource dataSource;
	@Autowired
	@Qualifier("ds1JdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;

	String SHA01(String pass) {
        String password = pass;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
	
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
                         .withProcedureName("getInstrumentCount");
            
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
					.withProcedureName("getInstrumentPage")
					.returningResultSet("RESULT_CURSOR", new IntrumentRowMapper());
			
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
					.withProcedureName("getInstrumentAll")
					.returningResultSet("RESULT_CURSOR", new IntrumentRowMapper());

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
	public ResultQuery insertIntrument(Intrument intrument, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
			if (intrument != null) {
				
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("insertInstrument");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", intrument.getNama())
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
	public ResultQuery updateIntrument(String inName, int inId, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("updateInstrument");
			
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
	public ResultQuery deleteIntrument(Intrument intrument, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("deleteInstrument");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inId", intrument.getId())
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
