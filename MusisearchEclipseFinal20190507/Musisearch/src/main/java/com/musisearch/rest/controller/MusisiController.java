package com.musisearch.rest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
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

import com.musisearch.rest.model.AdminUserService;
import com.musisearch.rest.model.Musisi;
import com.musisearch.rest.repository.MusisiListInterface;



@RestController
@RequestMapping("/api/musisi")
public class MusisiController {
	
	@Autowired(required=true)  
	private MusisiListInterface interfaceList;
	
	@RequestMapping(value="/rest/all", method=RequestMethod.GET)
    public Object selectMusisiAll(
    		@RequestParam(value = "inSearchValue", required=false) String inSearchValue,
    		@RequestParam(value = "inId", required=false) String inId,
    		@RequestParam(value = "inGender", required=false) String inGender,
    		@RequestParam(value = "inGenre", required=false) String inGenre,
    		@RequestParam(value = "inRegion", required=false) String inRegion,
    		@RequestParam(value = "inInstrument1", required=false) String inInstrument1,
    		@RequestParam(value = "inInstrument2", required=false) String inInstrument2,
    		@RequestParam(value = "inInstrument3", required=false) String inInstrument3,
    		@RequestParam(value = "likeBy", required=false) String likeBy
    ) {
		try {
			return interfaceList.getAll(inSearchValue, inId, inGender, inGenre, inRegion, inInstrument1, inInstrument2, inInstrument3, likeBy);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/insert", method=RequestMethod.POST)
    public Object insert(@Valid @RequestBody Musisi musisi) throws HttpMessageNotReadableException  
    {
		try {
			return interfaceList.insertRegion(musisi);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/rest/likes", method=RequestMethod.POST)
    public Object insertLike(
    		@RequestParam(value = "idMusisi", required=false) String idMusisi,
    		@RequestParam(value = "idLike", required=false) String idLike)
    {
		try {
			return interfaceList.insertLike(idMusisi, idLike);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/rest/update", method = RequestMethod.POST)
	public Object updateMusisi(
			@Valid @RequestBody Musisi musisi,
			HttpServletRequest request
			) {
		try {
			if (musisi.getImagePath() != null) {
				byte[] imageByte=Base64.decodeBase64(musisi.getImageString());
				@SuppressWarnings("deprecation")
				String directory = request.getSession().getServletContext().getRealPath("/");
				Path path = Paths.get(directory + "/images/" + musisi.getImagePath());
				FileOutputStream f = new FileOutputStream(path.toString());
	            f.write(imageByte);
	            f.close();
			}
			return interfaceList.updateRegion(musisi.getGenre(), musisi.getRegion(), musisi.getInstrument1(), musisi.getInstrument2(), musisi.getInstrument3(),musisi.getBiography(), musisi.getImagePath(), musisi.getImageString(), musisi.getId());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value="/rest/page", method=RequestMethod.GET)
    public Object selectSapAssetLocationId(
    		@RequestParam(value = "inSearchValue", required=false) String inSearchValue,
    		@RequestParam(value = "inId", required=false) String inId,
    		@RequestParam(value = "inGender", required=false) String inGender,
    		@RequestParam(value = "inGenre", required=false) String inGenre,
    		@RequestParam(value = "inRegion", required=false) String inRegion,
    		@RequestParam(value = "inInstrument1", required=false) String inInstrument1,
    		@RequestParam(value = "inInstrument2", required=false) String inInstrument2,
    		@RequestParam(value = "inInstrument3", required=false) String inInstrument3,
    		@RequestParam(value = "pageNumber", required=true) int pageNumber
    ) {
		try {
			return interfaceList.getByPage(pageNumber, inSearchValue, inId, inGender, inGenre, inRegion, inInstrument1, inInstrument2, inInstrument3);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView index(
			@RequestParam(value = "inSearchValue", required=false) String inSearchValue,
			@RequestParam(value = "inId", required=false) String inId,
    		@RequestParam(value = "inGender", required=false) String inGender,
    		@RequestParam(value = "inGenre", required=false) String inGenre,
    		@RequestParam(value = "inRegion", required=false) String inRegion,
    		@RequestParam(value = "inInstrument1", required=false) String inInstrument1,
    		@RequestParam(value = "inInstrument2", required=false) String inInstrument2,
    		@RequestParam(value = "inInstrument3", required=false) String inInstrument3,
    		@RequestParam(value = "pageNumber", required=true) int pageNumber
			) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("musisi/list");
		modelAndView.addObject("name", getUser().get(0));
		modelAndView.addObject("classActiveSettings", "active");
		modelAndView.addObject("activeTab", "musisiList");
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
