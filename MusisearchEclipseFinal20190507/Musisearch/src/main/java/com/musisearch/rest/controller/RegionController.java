package com.musisearch.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.musisearch.rest.model.Region;
import com.musisearch.rest.repository.AdminListInterface;
import com.musisearch.rest.repository.GenreListInterface;
import com.musisearch.rest.repository.RegionListInterface;



@RestController
@RequestMapping("/api/region")
public class RegionController {
	
	@Autowired(required=true)  
	private RegionListInterface listInterface;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectAll(
    		@RequestParam(value = "inNama", required=false) String inNama,
			@RequestParam(value = "inId", required=false) Integer inId
    ) {
		try {
			return listInterface.getAll(inNama, inId);
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
			return listInterface.getByPage(pageNumber, inNama, inId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView index(
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "inSearchValue", required=false) String inSearchValue
			) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("region/list");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "regionList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newGenre() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("region", new Region());
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("activeTab", "regionList");
		modelAndView.setViewName("region/new");
		return modelAndView;
	}
	
	@RequestMapping(value="/form", method = RequestMethod.GET)
    public ModelAndView form(
    		@RequestParam(value = "inRegionId", required = true) int inGenreId
    ){
    	ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("region/form");
		modelAndView.addObject("region", listInterface.getAll(null, inGenreId));
		modelAndView.addObject("idUser",getUser().get(1));
		modelAndView.addObject("name",getUser().get(0));
		modelAndView.addObject("classActiveSettings","active");
		modelAndView.addObject("activeTab", "regionList");
		return modelAndView;
    }
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delet(
			@RequestBody Region region 
			) {
		try {
			return listInterface.deleteRegion(region, getUser().get(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object updateAdmin(
			@RequestParam(value = "inNama", required = true) String inNama,
			@RequestParam(value = "inId", required = true) int inId
			) {
		
		try {
			return listInterface.updateRegion(inNama, inId, getUser().get(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveRegion(Region region) {
		listInterface.insertRegion(region, getUser().get(1));
		ModelAndView modelAndView = new ModelAndView("redirect:/api/region/list?pageNumber=1");
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "regionList");
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
