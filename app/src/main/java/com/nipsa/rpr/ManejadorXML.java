package com.nipsa.rpr;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

class ManejadorXML extends DefaultHandler {
    private StringBuilder cadenaData;
    private boolean esHoja = false;
    private int numHoja = 0;
    private boolean esFila = false;
    private int numFila = 0;
    private boolean esCelda = false;
    private Vector<String> datosFila;
    private boolean esData = false;

    /**
     * Gestiona el inicio del documento
     *
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        // Se inicializan las variables que se utilizarán
        cadenaData = new StringBuilder();
        datosFila = new Vector<String>();
    }

    /**
     * Gestiona el inicio de una etiqueta
     *
     * @param uri
     * @param nombreLocal
     * @param nombreCualif
     * @param atr
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String nombreLocal, String
                    nombreCualif, Attributes atr) throws SAXException {
        switch (nombreLocal){
            case "Worksheet": // Al iniciar la lectura de una hoja se ponen las filas a 0
                esHoja = true;
                numFila = 0;
                break;
            case "Row": // Al inicio de cada fila se resetean los datos de la fila recogidos
                esFila = true;
                datosFila.clear();
                break;
            case "Cell": // Al inicio de cada celda se resetean los datos de la celda recogidos
                esCelda = true;
                cadenaData.setLength(0);
                break;
            case "Data": // Se indica que se inicia la recogida de datos
                esData = true;
                break;
            default:
                break;
        }
    }

    /**
     * Gestiona los datos que hay en una etiqueta
     *
     * @param ch
     * @param comienzo
     * @param lon
     */
    @Override
    public void characters(char ch[], int comienzo, int lon) {
        // Si el dato recogido pertenece al dato de una celda
        if(esData && esCelda && esFila && esHoja){
            // se añade a la cadena
            cadenaData.append(ch, comienzo, lon);
        }
    }

    /**
     * Gestiona el final de una etiqueta
     *
     * @param uri
     * @param nombreLocal
     * @param nombreCualif
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String nombreLocal,
                           String nombreCualif) throws SAXException {
        switch (nombreLocal) {
            case "Worksheet":
                numHoja++;
                esHoja = false;
                break;
            case "Row":
                switch (numHoja) {
                    case 0:
                        if (numFila > 7) {
                            MostrarRevisiones.incluirEquipoEnDBRevisiones(datosFila);
                        }
                        break;
                    case 1:
                        if (numFila > 7){
                            datosFila.remove(0); // La primera columna de la segunda hoja no contiene información
                            MostrarRevisiones.incluirApoyoEnDBRevisiones(datosFila);
                        }
                        break;
                    case 2:
                        if (numFila > 8) {
                            MostrarRevisiones.incluirApoyoNoRevisableEnDBRevisiones(datosFila);
                        }
                        break;
                    default:
                        break;
                }
                numFila++;
                esFila = false;
                break;
            case "Cell":
                if (numFila > 0) {
                    esCelda = false;
                    datosFila.add(cadenaData.toString());
                }
                break;
            case "Data":
                esData = false;
                break;
            default:
                break;
        }
    }

    /**
     * Gestiona el final del documento
     *
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
    }

}