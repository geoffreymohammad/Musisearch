package com.project.android.musisearch.model;

public class Member {

    private int id, musisi, groupBand, createdby;
    private String namaMusisi, namaBand;

    public Member() {
    }

    public Member(int id, int musisi, int groupBand, int createdby, String namaMusisi, String namaBand) {
        this.id = id;
        this.musisi = musisi;
        this.groupBand = groupBand;
        this.createdby = createdby;
        this.namaMusisi = namaMusisi;
        this.namaBand = namaBand;
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

    public int getGroupBand() {
        return groupBand;
    }

    public void setGroupBand(int groupBand) {
        this.groupBand = groupBand;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
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
}
