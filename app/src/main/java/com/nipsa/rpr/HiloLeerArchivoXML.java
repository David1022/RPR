package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HiloLeerArchivoXML extends AsyncTask<File, Object, Object>{

    Aplicacion aplicacion = new Aplicacion();
    ProgressDialog pd;

    /**
     * Se muestra el diálogo
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(MostrarRevisiones.contexto);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Leyendo archivo XML...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    /**
     * Se leen los archivos
     *
     * @param archivo
     * @return
     */
    @Override
    protected Object doInBackground(File... archivo) {
        try {
            String nombreRevision = archivo[0].getName();
            //nombreRevision = nombreRevision.substring(nombreRevision.lastIndexOf("/") + 1, nombreRevision.lastIndexOf("."));
            InputSource entrada = new InputSource(
                    new InputStreamReader(
                            new FileInputStream(archivo[0])));
            SAXParserFactory fabrica = SAXParserFactory.newInstance();
            SAXParser parser = fabrica.newSAXParser();
            XMLReader lector = parser.getXMLReader();
            ManejadorXML manejadorXML = new ManejadorXML();
            lector.setContentHandler(manejadorXML);
            lector.parse(entrada);
        } catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al leer el archivo XML " + e.toString());
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        pd.dismiss();
    }

}

