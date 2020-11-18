package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.GroupBand;
import com.musisearch.rest.model.ResultQuery;

public interface GroupBandListInterface {
	ResultQuery getAll(String inNama, Integer inMusisi, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery getById(String inNama, Integer inMusisi, Integer inId);
	ResultQuery insertBand(GroupBand groupBand);
	ResultQuery updateBand(String inName, int id, Object created);
	ResultQuery deleteBand(GroupBand groupBand, Object created);
}
