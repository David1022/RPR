package com.nipsa.rpr;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David.Mendano on 01/08/2017.
 */

public class Tramo {

    private String revision, tramo, lng, lat;

    public Tramo (String revision, String tramo, String lng, String lat) {
        this.revision = revision;
        this.tramo = tramo;
        this.lng = lng;
        this.lat = lat;
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
