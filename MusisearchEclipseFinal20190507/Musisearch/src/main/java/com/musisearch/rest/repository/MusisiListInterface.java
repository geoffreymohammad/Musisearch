package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.Musisi;
import com.musisearch.rest.model.Region;
import com.musisearch.rest.model.ResultQuery;

public interface MusisiListInterface {
	ResultQuery getAll(String inSearchValue, String inId, String inGender, String inGenre, String inRegion, String inInstrument1, String inInstrument2, String inInstrument3, String likeBy);
	ResultQuery getByPage(int pageNumber,  String inSearchValue, String inId, String inGender, String inGenre, String inRegion, String inInstrument1, String inInstrument2, String inInstrument3);
	ResultQuery getById(int idAdmin);
	ResultQuery insertRegion(Musisi musisi);
	ResultQuery insertLike(String idMusisi, String idLike);
	ResultQuery updateRegion(int inGenre, int inRegion, int inInstrument1, int inInstrument2, int inInstrument3, String inBiography, String inImagePath, String inImageString, int id);
	ResultQuery deleteRegion(Musisi musisi, Object created);
}
