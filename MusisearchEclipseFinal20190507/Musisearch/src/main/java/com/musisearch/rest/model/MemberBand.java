package com.musisearch.rest.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonAutoDetect
public class MemberBand implements Serializable{
	private static final long serialVersionUID = 1L;
	int id;
	int musisi;
	String namaMusisi, namaBand;
	int groupBand;
	int createdBy;
	
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Jakarta")
	Date createdDate;
	
	String createdByName;
	int modifiedBy;
	
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Jakarta")
	Date modifiedDate;
	
	String modifiedByName;
	
	String modelName = "Genre";
	
	int status;
	int totalRow;
	
	public MemberBand() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getMusisi() {
		return musisi;
	}

	public void setMusisi(int musisi) {
		this.musisi = musisi;
	}

	public int getGroupBand() {
		return groupBand;
	}

	public void setGroupBand(int groupBand) {
		this.groupBand = groupBand;
	}
	
	public String getNamaMusisi() {
		return namaMusisi;
	}

	public void setNamaMusisi(String namaMusisi) {
		this.namaMusisi = namaMusisi;
	}

	public String getNamaBand() {
		return namaBand;
	}

	public void setNamaBand(String namaBand) {
		this.namaBand = namaBand;
	}

	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate =createdDate;
	}
	public int getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}
	
	
}
