package com.nipsa.rpr;

public class Tramo {

    private Integer orden;
    private String revision, tramo, lng, lat, color;

    public Tramo (Integer orden, String revision, String tramo, String lng, String lat, String color) {
        this.orden = orden;
        this.revision = revision;
        this.tramo = tramo;
        this.lat = lat;
        this.lng = lng;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
