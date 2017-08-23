package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.File;

public class HiloLeerArchivosCoord extends AsyncTask<File, Object, Object>{

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
        String nombreRevision = archivo[0].getName();
        nombreRevision = nombreRevision.substring(0, nombreRevision.indexOf("."));
        LectorCoord lector = new LectorCoord(nombreRevision);
        lector.leer(archivo[0]);

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

