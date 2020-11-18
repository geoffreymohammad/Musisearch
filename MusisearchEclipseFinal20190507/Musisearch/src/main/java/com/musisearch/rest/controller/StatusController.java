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

import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.AdminUserService;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Musisi;
import com.musisearch.rest.model.Status;
import com.musisearch.rest.repository.AdminListInterface;
import com.musisearch.rest.repository.GenreListInterface;
import com.musisearch.rest.repository.StatusListInterface;



@RestController
@RequestMapping("/api/status")
public class StatusController {
	
	@Autowired(required=true)  
	private StatusListInterface listInterface;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectStatusAll(
			@RequestParam(value = "inNama", required=false) String inNama
    ) {
		try {
			return listInterface.getAll(inNama);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/insert", method=RequestMethod.POST)
    public Object insert(@Valid @RequestBody Status status) throws HttpMessageNotReadableException  
    {
		try {
			return listInterface.insertStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Object> getUser(){
		List<Object> _listUser = new ArrayList<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AdminUserService currentUser = (AdminUserService)auth.getPrincipal();

		String _name = currentUser.getNama();
		int _id = currentUser.getId();
		
		_listUser.add(_name);
		_listUser.add(_id);
		return _listUser;
	}
	
}
