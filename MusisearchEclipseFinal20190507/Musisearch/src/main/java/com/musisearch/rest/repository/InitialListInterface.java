package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Initial;
import com.musisearch.rest.model.ResultQuery;

public interface InitialListInterface {
	ResultQuery getAll(String inNama, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery insertInitial(Initial initial, Object created);
	ResultQuery updateInitial(String inName, int id, Object created);
	ResultQuery deleteInitial(Initial initial, Object created);
}
