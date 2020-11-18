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
import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.rowmapper.AdminRowMapper;



@Repository
public class AdminListRepo implements AdminListInterface {

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
	public ResultQuery getByPage(int pageNumber,  String inSearchValue) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			
            Integer count = 0;
            
            Map<String, Object> result;
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                         .withProcedureName("getAdminCount");
            
            SqlParameterSource param = new MapSqlParameterSource()
                         .addValue("inSearchValue", inSearchValue);
            
            result = simpleJdbcCall.execute(param);
            if(result!=null){
                  count = Integer.parseInt(result.get("TOTALROW").toString()); 
            }
			
			ArrayList<Object> lstMap = new ArrayList<>();
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getAdminPage")
					.returningResultSet("RESULT_CURSOR", new AdminRowMapper());
			
			param = new MapSqlParameterSource()
					.addValue("inSearchValue", inSearchValue)
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
	public ResultQuery getAll(String inSearchValue) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			ArrayList<Object> lstMap = new ArrayList<>();
			Map<String, Object> result;
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getAdminAll")
					.returningResultSet("RESULT_CURSOR", new AdminRowMapper());

			SqlParameterSource param = new MapSqlParameterSource().addValue("inSearchValue", inSearchValue);

			result = simpleJdbcCall.execute(param);
			lstMap = (ArrayList) result.get("RESULT_CURSOR");
			double page = 0;
			if (lstMap != null && lstMap.size() > 0) {
				/*
				 * page = (Math.ceil((double)count/10));
				 * resultQuery.setIntegerResult((int)page);
				 */
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

	@Override
	public ResultQuery insertAdmin(Admin admin, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
			if (admin != null) {
				
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("insertAdmin");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", admin.getNama())
				.addValue("inEmail", admin.getEmail())
				.addValue("inPassword", SHA01(admin.getPassword()).toUpperCase())
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultQuery getById(int idAdmin) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);

		ResultQuery resultQuery = new ResultQuery();
		try {
			ArrayList<Object> lstMap = new ArrayList<>();
			Map<String, Object> result;
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getAdminId")
					.returningResultSet("RESULT_CURSOR", new AdminRowMapper());

			SqlParameterSource param = new MapSqlParameterSource().addValue("inIdAdmin", idAdmin);

			result = simpleJdbcCall.execute(param);
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
	public ResultQuery updateAdmin(String inNama, String inEmail, int inId, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
							simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("updateAdminId");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inNama", inNama)
				.addValue("inEmail", inEmail)
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
	public ResultQuery deleteAdmin(Admin admin, Object created) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ResultQuery resultQuery = new ResultQuery();
		try {
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("deleteAdminId");
			
				SqlParameterSource param = new MapSqlParameterSource()
				.addValue("inId", admin.getId())
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
