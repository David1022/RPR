package com.nipsa.rpr;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LectorCoord {

    private final String FOLDER_START = "<Folder>";
    private final String FOLDER_END = "</Folder>";
    private final String NAME_START = "<name>";
    private final String PLACEMARK_START = "<Placemark>";
    private final String PLACEMARK_END = "</Placemark>";
    private final String COORDINATES_START = "<coordinates>";
    private final String COORDINATES_END = "</coordinates>";

    private boolean esFolder, esPlacemark;
    private String tipo, nombre, nombreRevision;


    public LectorCoord (String revision) {

        this.esFolder = false;
        this.esPlacemark = false;
        this.tipo = "";
        this.nombreRevision = revision;
        this.nombre = "";

    }

    public void leer (File archivoCoord) {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(archivoCoord)));
            String texto = in.readLine();
            while (texto != null) {
                if (texto.contains(FOLDER_START)) {
                    esFolder = true;
                    texto = in.readLine();
                    continue;
                } else if (texto.contains(FOLDER_END)) {
                    esFolder = false;
                    tipo = "";
                    texto = in.readLine();
                    continue;
                } else if (texto.startsWith(NAME_START)) {
                    String data = leerNombre(texto);
                    if (esFolder) {
                        if (esPlacemark) {
                            nombre = data;
                        } else {
                            tipo = data;
                        }
                    }
                    texto = in.readLine();
                    continue;
                } else if (texto.contains(PLACEMARK_START)) {
                    esPlacemark = true;
                    texto = in.readLine();
                    continue;
                } else if (texto.contains(PLACEMARK_END)) {
                    esPlacemark = false;
                    nombre = "";
                    texto = in.readLine();
                    continue;
                } else if (texto.startsWith(COORDINATES_START)) {
                    String lng, lat;
                    if (tipo.equalsIgnoreCase("cds") || tipo.equalsIgnoreCase("apoyos")) {
                        lng = leerLng(texto);
                        lat = leerLat(texto);
                        MostrarRevisiones.actualizarCoordenadasEquipo(lng, lat, nombre);
                    } else if (tipo.equalsIgnoreCase("tramostraza")) {
                        texto = texto.substring(texto.indexOf(">") + 1);
                        while (!texto.equals(COORDINATES_END)) {
                            lng = texto.substring(0, texto.indexOf(","));
                            texto = texto.substring(texto.indexOf(",") + 1);
                            lat = texto.substring(0, texto.indexOf(","));
                            texto = texto.substring(texto.indexOf(",") + 1);
                            MostrarRevisiones.actualizarCoordenadasTramo(lng, lat, nombreRevision, nombre);
                            texto = texto.substring(2);
                        }
                    }
                    texto = in.readLine();
                    continue;
                } else {
                    texto = in.readLine();
                }
            }
            in.close();
        } catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al modificar el kml " + e.toString());
        }

    }

    private String leerNombre (String texto) {

        return texto.substring((texto.indexOf(">") + 1), texto.lastIndexOf("<"));

    }

    private String leerLng (String texto) {
        String lng;

        if (texto.startsWith(COORDINATES_START)) {
            lng = texto.substring((texto.indexOf(">") + 1), texto.indexOf(","));
        } else {
            lng = texto.substring(0, texto.indexOf(","));
        }

        return lng;

    }

    private String leerLat (String texto) {

        return texto.substring((texto.indexOf(",") + 1), texto.lastIndexOf(","));

    }

}
