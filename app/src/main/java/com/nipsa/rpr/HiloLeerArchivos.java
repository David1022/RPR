package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HiloLeerArchivos extends AsyncTask<File, Object, Object>{

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
        pd.setMessage("Leyendo archivos...");
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
            leerArchivoCoord(archivo[0]);
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

    /**
     * Método que recibe el archivo XML de referencia al cual se asociará el archivo KML. Leerá el
     * archivo KML y asociará las coordenadas recogidas al equipo correspondiente
     *
     * @param archivoXML con extensión XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */

    public void leerArchivoCoord (File archivoXML) {
        // Se recupera el nombre y la ruta del archivo XML para recuperar el archivo KML con el mismo nombre
        String nombreRevision = archivoXML.getName();
        nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
        String nombreKML = nombreRevision + ".kml";
        String ruta = archivoXML.getAbsolutePath();
        ruta = ruta.substring(0, ruta.lastIndexOf("/"));
        File archivoCoord = Aplicacion.recuperarArchivo(ruta, nombreKML);
        //Si se encuentra el archivo KML con el mismo nombre que el XML se leerá
        if (archivoCoord != null) {
            LectorCoord lector = new LectorCoord(nombreRevision, MostrarRevisiones.contexto);
            lector.leer(archivoCoord);

        } else { // Si no se encuentra se muestra un mensaje por pantalla para informar al usuario
            Log.e(Aplicacion.TAG, ("No se ha encontrado archivo KML asociado a la revision " + nombreRevision));
        }

    }

}

