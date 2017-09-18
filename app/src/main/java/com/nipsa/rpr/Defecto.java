package com.nipsa.rpr;

import java.util.Vector;

public class Defecto {

    private int id;
    private String nombreEquipo, nombreRevision, codigoDefecto, foto1, foto2;
    //private double medida, medidaTr2, medidaTr3;
    private String observaciones, ocurrencias, latitud, longitud, esDefecto;
    private String corregido, fechaCorreccion, tramo, medida, medidaTr2;
    private String medidaTr3, patUnidas, Rc1, Rc2, Rc3;

    public Defecto () {

    }

    public Defecto (int id, Vector<String> datos) {
        setId(id);
        setNombreEquipo(datos.elementAt(0));
        setNombreRevision(datos.elementAt(1));
        setCodigoDefecto(datos.elementAt(2));
        setFoto1(datos.elementAt(3));
        setFoto2(datos.elementAt(4));
        setMedida(datos.elementAt(5));
        setObservaciones(datos.elementAt(6));
        setOcurrencias(datos.elementAt(7));
        setLatitud(datos.elementAt(8));
        setLongitud(datos.elementAt(9));
        setEsDefecto(datos.elementAt(10));
        setCorregido(datos.elementAt(11));
        setFechaCorreccion(datos.elementAt(12));
        setTramo(datos.elementAt(13));
        setMedidaTr2(datos.elementAt(14));
        setMedidaTr3(datos.elementAt(15));
        setPatUnidas(datos.elementAt(16));
        setRc1(datos.elementAt(17));
        setRc2(datos.elementAt(18));
        setRc3(datos.elementAt(19));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombreRevision() {
        return nombreRevision;
    }

    public void setNombreRevision(String nombreRevision) {
        this.nombreRevision = nombreRevision;
    }

    public String getCodigoDefecto() {
        return codigoDefecto;
    }

    public void setCodigoDefecto(String codigoDefecto) {
        this.codigoDefecto = codigoDefecto;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getOcurrencias() {
        return ocurrencias;
    }

    public void setOcurrencias(String ocurrencias) {
        this.ocurrencias = ocurrencias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public String getEsDefecto() {
        return esDefecto;
    }

    public void setEsDefecto(String esDefecto) {
        this.esDefecto = esDefecto;
    }

    public String getCorregido() {
        return corregido;
    }

    public void setCorregido(String corregido) {
        this.corregido = corregido;
    }

    public String getFechaCorreccion() {
        return fechaCorreccion;
    }

    public void setFechaCorreccion(String fechaCorreccion) {
        this.fechaCorreccion = fechaCorreccion;
    }

    public String getTramo() {
        return tramo;
    }

    public void setTramo(String tramo) {
        this.tramo = tramo;
    }

    public String getMedidaTr2() {
        return medidaTr2;
    }

    public void setMedidaTr2(String medidaTr2) {
        this.medidaTr2 = medidaTr2;
    }

    public String getMedidaTr3() {
        return medidaTr3;
    }

    public void setMedidaTr3(String medidaTr3) {
        this.medidaTr3 = medidaTr3;
    }

    public String getPatUnidas() {
        return patUnidas;
    }

    public void setPatUnidas(String patUnidas) {
        this.patUnidas = patUnidas;
    }

    public String getRc1() {
        return Rc1;
    }

    public void setRc1(String rc1) {
        Rc1 = rc1;
    }

    public String getRc2() {
        return Rc2;
    }

    public void setRc2(String rc2) {
        Rc2 = rc2;
    }

    public String getRc3() {
        return Rc3;
    }

    public void setRc3(String rc3) {
        Rc3 = rc3;
    }
}