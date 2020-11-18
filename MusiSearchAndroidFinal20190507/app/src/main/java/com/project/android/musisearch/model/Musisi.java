package com.project.android.musisearch.model;

public class Musisi {

    private int id,gender, genre, region, instrument1, instrument2, instrument3, total, isLikes;
    private String nama, email, telp, biography, tglLahir, genderName, genreName, regionName, instrument1Name, instrument2Name,  instrument3Name, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

    public Musisi() {
    }

    public Musisi(int id, int gender, int genre, int region, int instrument1, int instrument2, int instrument3, String nama, String email, String telp, String biography, String tglLahir, String genderName, String genreName, String regionName, String instrument1Name, String instrument2Name, String instrument3Name, String createdDate, String createdByName, String modifiedDate, String modifiedByName, int total, int isLikes) {
        this.id = id;
        this.gender = gender;
        this.genre = genre;
        this.region = region;
        this.instrument1 = instrument1;
        this.instrument2 = instrument2;
        this.instrument3 = instrument3;
        this.nama = nama;
        this.email = email;
        this.telp = telp;
        this.biography = biography;
        this.tglLahir = tglLahir;
        this.genderName = genderName;
        this.genreName = genreName;
        this.regionName = regionName;
        this.instrument1Name = instrument1Name;
        this.instrument2Name = instrument2Name;
        this.instrument3Name = instrument3Name;
        this.createdDate = createdDate;
        this.createdByName = createdByName;
        this.modifiedDate = modifiedDate;
        this.modifiedByName = modifiedByName;
        this.total = total;
        this.isLikes = isLikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedByName() {
        return modifiedByName;
    }

    public void setModifiedByName(String modifiedByName) {
        this.modifiedByName = modifiedByName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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
