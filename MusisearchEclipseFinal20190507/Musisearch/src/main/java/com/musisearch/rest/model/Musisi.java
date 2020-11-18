package com.musisearch.rest.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonAutoDetect
public class Musisi implements Serializable{
	private static final long serialVersionUID = 1L;
	int id;
	String nama;
	String email;
	String telp;
	String imagePath;
	String imageString;
	String biography;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Asia/Jakarta")
	Date tglLahir;
	int gender;
	String genderName;
	int genre;
	String genreName;
	int region;
	String regionName;
	int instrument1;
	String instrument1Name;
	int instrument2;
	String instrument2Name;
	int instrument3;
	String instrument3Name;
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
	
	int status;
	int totalRow;
	int total;
	int isLikes;
	
	public Musisi() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelp() {
		return telp;
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public void setTelp(String telp) {
		this.telp = telp;
	}

	public Date getTglLahir() {
		return tglLahir;
	}

	public void setTglLahir(Date tglLahir) {
		this.tglLahir = tglLahir;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public int getInstrument1() {
		return instrument1;
	}

	public void setInstrument1(int instrument1) {
		this.instrument1 = instrument1;
	}

	public int getInstrument2() {
		return instrument2;
	}

	public void setInstrument2(int instrument2) {
		this.instrument2 = instrument2;
	}

	public int getInstrument3() {
		return instrument3;
	}

	public void setInstrument3(int instrument3) {
		this.instrument3 = instrument3;
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

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getInstrument1Name() {
		return instrument1Name;
	}

	public void setInstrument1Name(String instrument1Name) {
		this.instrument1Name = instrument1Name;
	}

	public String getInstrument2Name() {
		return instrument2Name;
	}

	public void setInstrument2Name(String instrument2Name) {
		this.instrument2Name = instrument2Name;
	}

	public String getInstrument3Name() {
		return instrument3Name;
	}

	public void setInstrument3Name(String instrument3Name) {
		this.instrument3Name = instrument3Name;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getIsLikes() {
		return isLikes;
	}

	public void setIsLikes(int isLikes) {
		this.isLikes = isLikes;
	}
	

	
	
}
