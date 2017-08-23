package com.nipsa.rpr;

import android.app.Application;

import java.util.Vector;

public class Revision {

    private int id;
    private String nombre;
    private String estado;
    private String inspector1;
    private String inspector2;
    private String colegiado = "Alejandro Rey Stolle";
    private String equiposUsados;
    private String metodologia;
    private String codigoNipsa;
    private String numTrabajo;
    private String codigoInspeccion;

    public Revision() {

    }

    public Revision(int id, Vector<String> datos) {
        if(datos.size() == 10) {
            setId(id);
            setNombre(datos.elementAt(0));
            setEstado(datos.elementAt(1));
            setInspector1(datos.elementAt(2));
            setInspector2(datos.elementAt(3));
            setColegiado(datos.elementAt(4));
            setEquiposUsados(datos.elementAt(5));
            setMetodologia(datos.elementAt(6));
            setCodigoNipsa(datos.elementAt(7));
            setNumTrabajo(datos.elementAt(8));
            setCodigoInspeccion(datos.elementAt(9));
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInspector1() {
        return inspector1;
    }

    public void setInspector1(String inspector1) {
        this.inspector1 = inspector1;
    }

    public String getInspector2() {
        return inspector2;
    }

    public void setInspector2(String inspector2) {
        this.inspector2 = inspector2;
    }

    public String getColegiado() {
        return colegiado;
    }

    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }

    public String getEquiposUsados() {
        return equiposUsados;
    }

    public void setEquiposUsados(String equiposUsados) {
        this.equiposUsados = equiposUsados;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getCodigoNipsa() {
        return codigoNipsa;
    }

    public void setCodigoNipsa(String codigoNipsa) {
        this.codigoNipsa = codigoNipsa;
    }

    public String getNumTrabajo() {
        return numTrabajo;
    }

    public void setNumTrabajo(String numTrabajo) {
        this.numTrabajo = numTrabajo;
    }

    public String getCodigoInspeccion() {
        return codigoInspeccion;
    }

    public void setCodigoInspeccion(String codigoInspeccion) {
        this.codigoInspeccion = codigoInspeccion;
    }

}
