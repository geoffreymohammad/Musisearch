package com.musisearch.rest.repository;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.AdminUserService;
import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.rowmapper.AdminRowMapper;

@Repository
public class AdminRepo implements UserDetailsService  {

	@Autowired
	@Qualifier("ds1")
	private DataSource dataSource;
	@Autowired
	@Qualifier("ds1JdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
    
   
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public UserDetails loadUserByUsername(String inEmail) throws UsernameNotFoundException {
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		ObjectMapper mapper = new ObjectMapper();
		ResultQuery resultQuery = new ResultQuery();
		UserDetails userDetails = null;
		AdminUserService adminUserService = null;
		try {
			ArrayList<Object> lstMap = new ArrayList<>();
			Map<String, Object> result;
			result = null;
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getUserAdmin")
					.returningResultSet("RESULT_CURSOR", new AdminRowMapper());

			SqlParameterSource param = new MapSqlParameterSource().addValue("inEmail", inEmail);

			result = simpleJdbcCall.execute(param);
			lstMap = (ArrayList) result.get("RESULT_CURSOR");
			
			if (lstMap.size() <= 0) {
				throw new UsernameNotFoundException("User " + inEmail + " was not found in the database");
			}
			if (lstMap != null && lstMap.size() > 0) {
				/*
				 * page = (Math.ceil((double)count/10));
				 * resultQuery.setIntegerResult((int)page);
				 */
				
				
				List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			
				GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
				grantList.add(authority);
				resultQuery.setLstObject(lstMap);
				
				Admin smSp = mapper.convertValue(resultQuery.getLstObject().get(0), new TypeReference<Admin>() { });
				adminUserService = new AdminUserService(smSp.getEmail(), smSp.getPassword().toLowerCase(), true, true,
						true, true, grantList, smSp.getId(), smSp.getNama(), smSp.getEmail(), smSp.getCreatedBy(), smSp.getCreatedDate(),smSp.getModifiedBy(),
						smSp.getModifiedDate(), smSp.getStatus());
				
				
				//String username = smSp.getUserName();
				String password = smSp.getPassword();
				//EncrytedPasswordUtils enUtils = new EncrytedPasswordUtils();
				//String _pass =  enUtils.encrytePassword(password);
				//String _pass = enUtils.encrytePassword(smSp.getPassword());
				//String _pass =  enUtils.encrytePassword("123");
				//String _pass = "123";
				userDetails = (UserDetails) new User(smSp.getEmail(), password.toLowerCase(), grantList);
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

		return adminUserService;
	}
	
}
