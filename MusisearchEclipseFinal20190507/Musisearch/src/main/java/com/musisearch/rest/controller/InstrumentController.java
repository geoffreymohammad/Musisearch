package com.musisearch.rest.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.musisearch.rest.model.AdminUserService;
import com.musisearch.rest.model.Intrument;
import com.musisearch.rest.repository.IntrumentListInterface;



@RestController
@RequestMapping("/api/instrument")
public class InstrumentController {
	
	@Autowired(required=true)  
	private IntrumentListInterface interfaceList;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectAll(
    		@RequestParam(value = "inNama", required=false) String inNama,
			@RequestParam(value = "inId", required=false) Integer inId
    ) {
		try {
			return interfaceList.getAll(inNama, inId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/page", method=RequestMethod.GET)
    public Object selectByPage(
    		@RequestParam(value = "inNama", required=false) String inNama,
    		@RequestParam(value = "inId", required=false) Integer inId,
    		@RequestParam(value = "pageNumber", required=true) int pageNumber
    ) {
		try {
			return interfaceList.getByPage(pageNumber, inNama, inId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/rest/delete", method = RequestMethod.POST)
	public Object delet(
			@RequestBody Intrument intrument 
			) {
		try {
			return interfaceList.deleteIntrument(intrument, 6);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/rest/update", method = RequestMethod.POST)
	public Object updateAdmin(
			@RequestParam(value = "inNama", required = true) String inNama,
			@RequestParam(value = "inId", required = true) int inId
			) {
		
		try {
			return interfaceList.updateIntrument(inNama, inId, 6);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveRegion(Intrument intrument) {
		interfaceList.insertIntrument(intrument, getUser().get(1));
		ModelAndView modelAndView = new ModelAndView("redirect:/api/instrument/list?pageNumber=1");
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "instrumentList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView index(
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "inSearchValue", required=false) String inSearchValue
			) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("instrument/list");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "instrumentList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newGenre() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("instrument", new Intrument());
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("activeTab", "instrumentList");
		modelAndView.setViewName("instrument/new");
		return modelAndView;
	}
	
	@RequestMapping(value="/form", method = RequestMethod.GET)
    public ModelAndView form(
    		@RequestParam(value = "inInstrumentId", required = true) int inInstrumentId
    ){
    	ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("instrument/form");
		modelAndView.addObject("instrument", interfaceList.getAll(null, inInstrumentId));
		modelAndView.addObject("idUser",getUser().get(1));
		modelAndView.addObject("name",getUser().get(0));
		modelAndView.addObject("classActiveSettings","active");
		modelAndView.addObject("activeTab", "instrumentList");
		return modelAndView;
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
