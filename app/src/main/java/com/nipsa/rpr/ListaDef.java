package com.nipsa.rpr;

import java.util.Vector;

public class ListaDef {

    private String tipoInstalacion;
    private String codigoEndesa2010;
    private String CodigoEndesa2012;
    private String criticidadCatalunya;
    private String codigoDecreto328;
    private String descripcionEndesa;
    private String observaciones;
    private String correccionInmediata;
    private String defectesEstrategics;
    private String DefectesMajorsMenors;

    public ListaDef (Vector<String> datos) {
        setTipoInstalacion(datos.elementAt(0));
        setCodigoEndesa2010(datos.elementAt(1));
        setCodigoEndesa2012(datos.elementAt(2));
        setCriticidadCatalunya(datos.elementAt(3));
        setCodigoDecreto328(datos.elementAt(4));
        setDescripcionEndesa(datos.elementAt(5));
        setObservaciones(datos.elementAt(6));
        setCorreccionInmediata(datos.elementAt(7));
        setDefectesEstrategics(datos.elementAt(8));
        setDefectesMajorsMenors(datos.elementAt(9));
    }

    public String getTipoInstalacion() {
        return tipoInstalacion;
    }

    public void setTipoInstalacion(String tipoInstalacion) {
        this.tipoInstalacion = tipoInstalacion;
    }

    public String getCodigoEndesa2010() {
        return codigoEndesa2010;
    }

    public void setCodigoEndesa2010(String codigoEndesa2010) {
        this.codigoEndesa2010 = codigoEndesa2010;
    }

    public String getCodigoEndesa2012() {
        return CodigoEndesa2012;
    }

    public void setCodigoEndesa2012(String codigoEndesa2012) {
        CodigoEndesa2012 = codigoEndesa2012;
    }

    public String getCriticidadCatalunya() {
        return criticidadCatalunya;
    }

    public void setCriticidadCatalunya(String criticidadCatalunya) {
        this.criticidadCatalunya = criticidadCatalunya;
    }

    public String getCodigoDecreto328() {
        return codigoDecreto328;
    }

    public void setCodigoDecreto328(String codigoDecreto328) {
        this.codigoDecreto328 = codigoDecreto328;
    }

    public String getDescripcionEndesa() {
        return descripcionEndesa;
    }

    public void setDescripcionEndesa(String descripcionEndesa) {
        this.descripcionEndesa = descripcionEndesa;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCorreccionInmediata() {
        return correccionInmediata;
    }

    public void setCorreccionInmediata(String correccionInmediata) {
        this.correccionInmediata = correccionInmediata;
    }

    public String getDefectesEstrategics() {
        return defectesEstrategics;
    }

    public void setDefectesEstrategics(String defectesEstrategics) {
        this.defectesEstrategics = defectesEstrategics;
    }

    public String getDefectesMajorsMenors() {
        return DefectesMajorsMenors;
    }

    public void setDefectesMajorsMenors(String defectesMajorsMenors) {
        DefectesMajorsMenors = defectesMajorsMenors;
    }

}
