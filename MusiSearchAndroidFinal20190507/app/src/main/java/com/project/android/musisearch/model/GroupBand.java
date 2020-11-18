package com.project.android.musisearch.model;

public class GroupBand {

    private int id,musisi;
    private String nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

    public GroupBand(int id, int musisi, String nama, String createdDate, String createdByName, String modifiedDate, String modifiedByName, String modelName) {
        this.id = id;
        this.musisi = musisi;
        this.nama = nama;
        this.createdDate = createdDate;
        this.createdByName = createdByName;
        this.modifiedDate = modifiedDate;
        this.modifiedByName = modifiedByName;
        this.modelName = modelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMusisi() {
        return musisi;
    }

    public void setMusisi(int musisi) {
        this.musisi = musisi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
}
