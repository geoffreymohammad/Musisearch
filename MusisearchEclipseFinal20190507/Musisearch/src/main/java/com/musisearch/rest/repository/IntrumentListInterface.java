package com.musisearch.rest.repository;


import com.musisearch.rest.model.Intrument;
import com.musisearch.rest.model.ResultQuery;

public interface IntrumentListInterface {
	ResultQuery getAll(String inNama, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery getById(int idAdmin);
	ResultQuery insertIntrument(Intrument intrument, Object created);
	ResultQuery updateIntrument(String inName, int inId, Object created);
	ResultQuery deleteIntrument(Intrument intrument, Object created);
}
