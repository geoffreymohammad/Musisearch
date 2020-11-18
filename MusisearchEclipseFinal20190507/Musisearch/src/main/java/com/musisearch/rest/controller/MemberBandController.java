package com.musisearch.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.AdminUserService;
import com.musisearch.rest.model.GroupBand;
import com.musisearch.rest.model.MemberBand;
import com.musisearch.rest.model.Musisi;
import com.musisearch.rest.repository.AdminListInterface;
import com.musisearch.rest.repository.GenreListInterface;
import com.musisearch.rest.repository.GroupBandListInterface;
import com.musisearch.rest.repository.MemberBandListInterface;
import com.musisearch.rest.repository.MusisiListInterface;
import com.musisearch.rest.repository.RegionListInterface;



@RestController
@RequestMapping("/api/memberband")
public class MemberBandController {
	
	@Autowired(required=true)  
	private MemberBandListInterface interfaceList;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectAdminAll(
			@RequestParam(value = "inMusisi", required=false) Integer inMusisi,
			@RequestParam(value = "inId", required=false) Integer inId
    ) {
		try {
			return interfaceList.getAll(inMusisi, inId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/insert", method=RequestMethod.POST)
    public Object insert(@Valid @RequestBody MemberBand band) throws HttpMessageNotReadableException  
    {
		try {
			return interfaceList.insertMember(band);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
