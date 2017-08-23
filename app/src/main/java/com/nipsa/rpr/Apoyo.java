package com.nipsa.rpr;

import java.util.Vector;

public class Apoyo {

    private int id;
    private String codigoApoyo, observaciones, maxTension, comboMaxTension, material;
    private String comboMaterial, estructura, comboEstructura, alturaApoyo, husoApoyo;
    private String husoCombo, coordenadaXUTMApoyo, coordenadaYUTMApoyo, tipoInstalacion, nombreInstalacion;
    private String codigoTramo, latitud, longitud, nombreRevision, nombreEquipo;

    public Apoyo() {

    }

    public Apoyo (int id, Vector<String> datos) {
        if (datos.size()==20) {
            setId(id);
            setCodigoApoyo(datos.elementAt(0));
            setObservaciones(datos.elementAt(1));
            setMaxTension(datos.elementAt(2));
            setComboMaxTension(datos.elementAt(3));
            setMaterial(datos.elementAt(4));
            setComboMaterial(datos.elementAt(5));
            setEstructura(datos.elementAt(6));
            setComboEstructura(datos.elementAt(7));
            setAlturaApoyo(datos.elementAt(8));
            setHusoApoyo(datos.elementAt(9));
            setHusoCombo(datos.elementAt(10));
            setCoordenadaXUTMApoyo(datos.elementAt(11));
            setCoordenadaYUTMApoyo(datos.elementAt(12));
            setTipoInstalacion(datos.elementAt(13));
            setNombreInstalacion(datos.elementAt(14));
            setCodigoTramo(datos.elementAt(15));
            setLatitud(datos.elementAt(16));
            setLongitud(datos.elementAt(17));
            setNombreRevision(datos.elementAt(18));
            setNombreEquipo(datos.elementAt(19));

        } else {
            Aplicacion.print("Error en el vector pasado: Se han pasado " + datos.size() + " datos");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoApoyo() {
        return codigoApoyo;
    }

    public void setCodigoApoyo(String codigoApoyo) {
        this.codigoApoyo = codigoApoyo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMaxTension() {
        return maxTension;
    }

    public void setMaxTension(String maxTension) {
        this.maxTension = maxTension;
    }

    public String getComboMaxTension() {
        return comboMaxTension;
    }

    public void setComboMaxTension(String comboMaxTension) {
        this.comboMaxTension = comboMaxTension;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getComboMaterial() {
        return comboMaterial;
    }

    public void setComboMaterial(String comboMaterial) {
        this.comboMaterial = comboMaterial;
    }

    public String getEstructura() {
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public String getComboEstructura() {
        return comboEstructura;
    }

    public void setComboEstructura(String comboEstructura) {
        this.comboEstructura = comboEstructura;
    }

    public String getAlturaApoyo() {
        return alturaApoyo;
    }

    public void setAlturaApoyo(String alturaApoyo) {
        this.alturaApoyo = alturaApoyo;
    }

    public String getHusoApoyo() {
        return husoApoyo;
    }

    public void setHusoApoyo(String husoApoyo) {
        this.husoApoyo = husoApoyo;
    }

    public String getHusoCombo() {
        return husoCombo;
    }

    public void setHusoCombo(String husoCombo) {
        this.husoCombo = husoCombo;
    }

    public String getCoordenadaXUTMApoyo() {
        return coordenadaXUTMApoyo;
    }

    public void setCoordenadaXUTMApoyo(String coordenadaXUTMApoyo) {
        this.coordenadaXUTMApoyo = coordenadaXUTMApoyo;
    }

    public String getCoordenadaYUTMApoyo() {
        return coordenadaYUTMApoyo;
    }

    public void setCoordenadaYUTMApoyo(String coordenadaYUTMApoyo) {
        this.coordenadaYUTMApoyo = coordenadaYUTMApoyo;
    }

    public String getTipoInstalacion() {
        return tipoInstalacion;
    }

    public void setTipoInstalacion(String tipoInstalacion) {
        this.tipoInstalacion = tipoInstalacion;
    }

    public String getNombreInstalacion() {
        return nombreInstalacion;
    }

    public void setNombreInstalacion(String nombreInstalacion) {
        this.nombreInstalacion = nombreInstalacion;
    }

    public String getCodigoTramo() {
        return codigoTramo;
    }

    public void setCodigoTramo(String codigoTramo) {
        this.codigoTramo = codigoTramo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNombreRevision() {
        return nombreRevision;
    }

    public void setNombreRevision(String nombreRevision) {
        this.nombreRevision = nombreRevision;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

}
