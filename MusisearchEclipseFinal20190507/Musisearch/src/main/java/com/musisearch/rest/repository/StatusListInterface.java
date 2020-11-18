package com.musisearch.rest.repository;


import com.musisearch.rest.model.ResultQuery;
import com.musisearch.rest.model.Status;

public interface StatusListInterface {
	ResultQuery getAll(String inNama);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery insertStatus(Status status);
	ResultQuery updateStatus(String inName, int id, Object created);
	ResultQuery deleteStatus(Status status, Object created);
}
