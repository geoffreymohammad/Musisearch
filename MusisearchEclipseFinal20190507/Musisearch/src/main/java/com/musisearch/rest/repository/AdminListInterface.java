package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.ResultQuery;

public interface AdminListInterface {
	ResultQuery getAll(String inSearchValue);
	ResultQuery getByPage(int pageNumber,  String inSearchValue);
	ResultQuery getById(int idAdmin);
	ResultQuery insertAdmin(Admin admin, Object created);
	ResultQuery updateAdmin(String inName, String inEmail, int id, Object created);
	ResultQuery deleteAdmin(Admin admin, Object created);
}
