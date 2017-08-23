package com.nipsa.rpr;

import java.util.Vector;

public class Equipo {

    private final int NUM_COLUMNAS_TABLA_EQUIPO = 35;
    private int id;
    private String tipoInstalcion, codigoBDE, descripcionInstalacion, posicionTramo, codigoTramo;
    private String descripcionTramo, equipoApoyo, fechaInspeccion, defectoMedida, descripcionDefectoMedida;
    private String crit, ocurrenciasMedida, estadoInstalacion, trabajoInspeccion, valoracion;
    private String importe, limiteCorreccion, trabajoCorreccion, fechaCorreccion, D;
    private String C, codigoInspeccion, observaciones, documentosAsociar, descripcionDocumentos;
    private String fechaAlta, tpl, kmAereos, estado, nombreRevision;
    private String nombreEquipo, hayCruces, hayManiobra, hayConversion, tipoEquipo;

    public Equipo() {

    }

    public Equipo (int id, Vector<String> datos) {
        if(datos.size() == NUM_COLUMNAS_TABLA_EQUIPO) {
            setId(id);
            setTipoInstalcion(datos.elementAt(0));
            setCodigoBDE(datos.elementAt(1));
            setDescripcionInstalacion(datos.elementAt(2));
            setPosicionTramo(datos.elementAt(3));
            setCodigoTramo(datos.elementAt(4));
            setDescripcionTramo(datos.elementAt(5));
            setEquipoApoyo(datos.elementAt(6));
            setFechaInspeccion(datos.elementAt(7));
            setDefectoMedida(datos.elementAt(8));
            setDescripcionDefectoMedida(datos.elementAt(9));
            setCrit(datos.elementAt(10));
            setOcurrenciasMedida(datos.elementAt(11));
            setEstadoInstalacion(datos.elementAt(12));
            setTrabajoInspeccion(datos.elementAt(13));
            setValoracion(datos.elementAt(14));
            setImporte(datos.elementAt(15));
            setLimiteCorreccion(datos.elementAt(16));
            setTrabajoCorreccion(datos.elementAt(17));
            setFechaCorreccion(datos.elementAt(18));
            setD(datos.elementAt(19));
            setC(datos.elementAt(20));
            setCodigoInspeccion(datos.elementAt(21));
            setObservaciones(datos.elementAt(22));
            setDocumentosAsociar(datos.elementAt(23));
            setDescripcionDocumentos(datos.elementAt(24));
            setFechaAlta(datos.elementAt(25));
            setTpl(datos.elementAt(26));
            setKmAereos(datos.elementAt(27));
            setEstado(datos.elementAt(28));
            setNombreRevision(datos.elementAt(29));
            if(datos.elementAt(30).equals("")){
                if(getTipoInstalcion().equals("L")){
                    setNombreEquipo(getEquipoApoyo());
                } else {
                    setNombreEquipo(getCodigoBDE());
                }
            } else {
                setNombreEquipo(datos.elementAt(30));
            }
            setHayCruces(datos.elementAt(31));
            setHayManiobra(datos.elementAt(32));
            setHayConversion(datos.elementAt(33));
            setTipoEquipo(datos.elementAt(34));
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

    public String getTipoInstalcion() {
        return tipoInstalcion;
    }

    public void setTipoInstalcion(String tipoInstalcion) {
        this.tipoInstalcion = tipoInstalcion;
    }

    public String getCodigoBDE() {
        return codigoBDE;
    }

    public void setCodigoBDE(String codigoBDE) {
        this.codigoBDE = codigoBDE;
    }

    public String getDescripcionInstalacion() {
        return descripcionInstalacion;
    }

    public void setDescripcionInstalacion(String descripcionInstalacion) {
        this.descripcionInstalacion = descripcionInstalacion;
    }

    public String getPosicionTramo() {
        return posicionTramo;
    }

    public void setPosicionTramo(String posicionTramo) {
        this.posicionTramo = posicionTramo;
    }

    public String getCodigoTramo() {
        return codigoTramo;
    }

    public void setCodigoTramo(String codigoTramo) {
        this.codigoTramo = codigoTramo;
    }

    public String getDescripcionTramo() {
        return descripcionTramo;
    }

    public void setDescripcionTramo(String descripcionTramo) {
        this.descripcionTramo = descripcionTramo;
    }

    public String getEquipoApoyo() {
        return equipoApoyo;
    }

    public void setEquipoApoyo(String equipoApoyo) {
        this.equipoApoyo = equipoApoyo;
    }

    public String getFechaInspeccion() {
        return fechaInspeccion;
    }

    public void setFechaInspeccion(String fechaInspeccion) {
        this.fechaInspeccion = fechaInspeccion;
    }

    public String getDefectoMedida() {
        return defectoMedida;
    }

    public void setDefectoMedida(String defectoMedida) {
        this.defectoMedida = defectoMedida;
    }

    public String getDescripcionDefectoMedida() {
        return descripcionDefectoMedida;
    }

    public void setDescripcionDefectoMedida(String descripcionDefectoMedida) {
        this.descripcionDefectoMedida = descripcionDefectoMedida;
    }

    public String getCrit() {
        return crit;
    }

    public void setCrit(String crit) {
        this.crit = crit;
    }

    public String getOcurrenciasMedida() {
        return ocurrenciasMedida;
    }

    public void setOcurrenciasMedida(String ocurrenciasMedida) {
        this.ocurrenciasMedida = ocurrenciasMedida;
    }

    public String getEstadoInstalacion() {
        return estadoInstalacion;
    }

    public void setEstadoInstalacion(String estadoInstalacion) {
        this.estadoInstalacion = estadoInstalacion;
    }

    public String getTrabajoInspeccion() {
        return trabajoInspeccion;
    }

    public void setTrabajoInspeccion(String trabajoInspeccion) {
        this.trabajoInspeccion = trabajoInspeccion;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getLimiteCorreccion() {
        return limiteCorreccion;
    }

    public void setLimiteCorreccion(String limiteCorreccion) {
        this.limiteCorreccion = limiteCorreccion;
    }

    public String getTrabajoCorreccion() {
        return trabajoCorreccion;
    }

    public void setTrabajoCorreccion(String trabajoCorreccion) {
        this.trabajoCorreccion = trabajoCorreccion;
    }

    public String getFechaCorreccion() {
        return fechaCorreccion;
    }

    public void setFechaCorreccion(String fechaCorreccion) {
        this.fechaCorreccion = fechaCorreccion;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getCodigoInspeccion() {
        return codigoInspeccion;
    }

    public void setCodigoInspeccion(String codigoInspeccion) {
        this.codigoInspeccion = codigoInspeccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDocumentosAsociar() {
        return documentosAsociar;
    }

    public void setDocumentosAsociar(String documentosAsociar) {
        this.documentosAsociar = documentosAsociar;
    }

    public String getDescripcionDocumentos() {
        return descripcionDocumentos;
    }

    public void setDescripcionDocumentos(String descripcionDocumentos) {
        this.descripcionDocumentos = descripcionDocumentos;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getTpl() {
        return tpl;
    }

    public void setTpl(String tpl) {
        this.tpl = tpl;
    }

    public String getKmAereos() {
        return kmAereos;
    }

    public void setKmAereos(String kmAereos) {
        this.kmAereos = kmAereos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getHayCruces() {
        return hayCruces;
    }

    public void setHayCruces(String hayCruces) {
        this.hayCruces = hayCruces;
    }

    public String getHayManiobra() {
        return hayManiobra;
    }

    public void setHayManiobra(String hayManiobra) {
        this.hayManiobra = hayManiobra;
    }

    public String getHayConversion() {
        return hayConversion;
    }

    public void setHayConversion(String hayConversion) {
        this.hayConversion = hayConversion;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

}
