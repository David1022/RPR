package com.nipsa.rpr;

public class Tramo {

    private Integer orden;
    private String revision, tramo, lng, lat;

    public Tramo (Integer orden, String revision, String tramo, String lng, String lat) {
        this.orden = orden;
        this.revision = revision;
        this.tramo = tramo;
        this.lng = lng;
        this.lat = lat;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getTramo() {
        return tramo;
    }

    public void setTramo(String tramo) {
        this.tramo = tramo;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
