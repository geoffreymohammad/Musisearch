package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.ResultQuery;

public interface GenreListInterface {
	ResultQuery getAll(String inNama, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery insertGenre(Genre genre, Object created);
	ResultQuery updateGenre(String inName, int id, Object created);
	ResultQuery deleteGenre(Genre genre, Object created);
}
