package com.musisearch.rest.repository;


import com.musisearch.rest.model.Admin;
import com.musisearch.rest.model.Genre;
import com.musisearch.rest.model.GroupBand;
import com.musisearch.rest.model.MemberBand;
import com.musisearch.rest.model.ResultQuery;

public interface MemberBandListInterface {
	ResultQuery getAll(Integer inMusisi, Integer inId);
	ResultQuery getByPage(int pageNumber,  String inNama, Integer inId);
	ResultQuery insertMember(MemberBand memberBand);
	ResultQuery updateMember(String inName, int id, Object created);
	ResultQuery deleteMember(MemberBand memberBand, Object created);
}
