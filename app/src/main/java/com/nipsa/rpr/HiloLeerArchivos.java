package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nipsa.rpr.ExcelHandler.ExcelReader;

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

public class HiloLeerArchivos extends AsyncTask<Object, Object, Object>{

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
        pd.setMessage("Leyendo archivos\nEsta acción puede tardar unos minutos...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    /**
     * Se leen los archivos
     *
     * @param objects
     * @return
     */
    @Override
    protected Object doInBackground(Object... objects) {
        try {
            Context context = (Context) objects[0];
            File archivo = (File) objects[1];
//            InputSource entrada = new InputSource(
//                    new InputStreamReader(
//                            new FileInputStream(archivo)));
//            SAXParserFactory fabrica = SAXParserFactory.newInstance();
//            SAXParser parser = fabrica.newSAXParser();
//            XMLReader lector = parser.getXMLReader();
//            ManejadorXML manejadorXML = new ManejadorXML();
//            lector.setContentHandler(manejadorXML);
//            lector.parse(entrada);
            new ExcelReader(context, archivo).readExcelFile();
            leerArchivoCoord(archivo);
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
     * @param archivoExcel con extensión excel
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */

    public void leerArchivoCoord (File archivoExcel) {
        // Se recupera el nombre y la ruta del archivo XML para recuperar el archivo KML con el mismo nombre
        String nombreRevision = archivoExcel.getName();
        nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
        String nombreKML = nombreRevision + ".kml";
        String ruta = archivoExcel.getAbsolutePath();
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

