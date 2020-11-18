package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Region;
import com.musisearch.rest.model.ResultQuery;

public interface RegionListInterface {
	ResultQuery getAll(String inNama, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery getById(int idAdmin);
	ResultQuery insertRegion(Region region, Object created);
	ResultQuery updateRegion(String inName, int id, Object created);
	ResultQuery deleteRegion(Region region, Object created);
}
