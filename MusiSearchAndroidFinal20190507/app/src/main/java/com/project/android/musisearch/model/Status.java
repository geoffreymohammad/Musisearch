package com.project.android.musisearch.model;

public class Status {

    private int id,musisi, initialStatus;
    private String musisiName, email, telp, tglLahir, initialStatusName, nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

    public Status(int id, int musisi, int initialStatus, String musisiName, String email, String telp, String tglLahir, String initialStatusName, String nama, String createdDate, String createdByName, String modifiedDate, String modifiedByName, String modelName) {
        this.id = id;
        this.musisi = musisi;
        this.initialStatus = initialStatus;
        this.musisiName = musisiName;
        this.email = email;
        this.telp = telp;
        this.tglLahir = tglLahir;
        this.initialStatusName = initialStatusName;
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

    public int getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(int initialStatus) {
        this.initialStatus = initialStatus;
    }

    public String getMusisiName() {
        return musisiName;
    }

    public void setMusisiName(String musisiName) {
        this.musisiName = musisiName;
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

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getInitialStatusName() {
        return initialStatusName;
    }

    public void setInitialStatusName(String initialStatusName) {
        this.initialStatusName = initialStatusName;
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
