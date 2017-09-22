package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by David.Mendano on 24/08/2017.
 */

public class HiloFinalizarRevision extends AsyncTask<Revision, Object, Object> {
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
        pd.setTitle("Espere por favor");
        pd.setMessage("Generando archivos y moviendo fotos\nEsta acción puede tardar unos minutos...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    /**
     * Se generan los archivos
     * @param revision
     * @return
     */
    @Override
    protected Object doInBackground(Revision[] revision) {
        String nombreRevision = revision[0].getNombre();

//        pd.setMessage("Backup");
        Aplicacion.backupBaseDatos();
//        pd.setMessage("Coord");
        Aplicacion.convertirCoordenadasApoyos(nombreRevision);
//        pd.setMessage("XML");
        Aplicacion.generarXML(nombreRevision);
//        pd.setMessage("KMZ");
        Aplicacion.generarKML(nombreRevision);
//        pd.setMessage("TXT Equipos");
        Aplicacion.generarArchivoEquiposTXT(nombreRevision);
//        pd.setMessage("TXT Defectos");
        Aplicacion.generarArchivoDefectosTXT(nombreRevision);
//        pd.setMessage("Moviendo Fotos");
        Aplicacion.moverFotos(revision[0]);

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

    @Override
    protected void onCancelled() {
        super.onCancelled();

        pd.dismiss();
    }

}
