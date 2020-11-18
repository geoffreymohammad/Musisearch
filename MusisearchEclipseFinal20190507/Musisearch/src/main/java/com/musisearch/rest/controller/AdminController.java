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
import com.musisearch.rest.repository.AdminListInterface;



@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired(required=true)  
	private AdminListInterface adminListInterface;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectAdminAll(
			@RequestParam(value = "inSearchValue", required=false) String inSearchValue
    ) {
		try {
			return adminListInterface.getAll(inSearchValue);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/id", method=RequestMethod.GET)
    public Object selectAdminId(
    		@RequestParam(value = "inIdAdmin", required = true) int inIdAdmin
    ) {
		try {
			return adminListInterface.getById(inIdAdmin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/page", method=RequestMethod.GET)
    public Object selectSapAssetLocationId(
    		@RequestParam(value = "inSearchValue", required=false) String inSearchValue,
    		@RequestParam(value = "pageNumber", required=true) int pageNumber
    ) {
		try {
			return adminListInterface.getByPage(pageNumber, inSearchValue);
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
		modelAndView.setViewName("admin/list");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "adminList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newAdmin() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("admin", new Admin());
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("activeTab", "adminList");
		modelAndView.setViewName("admin/new");
		return modelAndView;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveAdmin(Admin admin) {
		adminListInterface.insertAdmin(admin, getUser().get(1));
		ModelAndView modelAndView = new ModelAndView("redirect:/api/admin/list?pageNumber=1");
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "adminList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object updateAdmin(
			@RequestParam(value = "inNama", required = true) String inNama,
			@RequestParam(value = "inEmail", required = true) String inEmail,
			@RequestParam(value = "inId", required = true) int inId
			) {
		
		try {
			return adminListInterface.updateAdmin(inNama, inEmail, inId, getUser().get(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object deleteAdmin(
			@RequestBody Admin admin 
			) {
		try {
			return adminListInterface.deleteAdmin(admin, getUser().get(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/form", method = RequestMethod.GET)
    public ModelAndView formAdmin(
    		@RequestParam(value = "inAdminId", required = true) int inAdminId
    ){
    	ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/form");
		modelAndView.addObject("admin", adminListInterface.getById(inAdminId));
		modelAndView.addObject("idUser",getUser().get(1));
		modelAndView.addObject("name",getUser().get(0));
		modelAndView.addObject("classActiveSettings","active");
		modelAndView.addObject("activeTab", "adminList");
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
