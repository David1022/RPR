package com.nipsa.rpr;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class HiloBackup extends AsyncTask<String, Object, Object> {

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
        pd.setMessage("Generando archivos...");
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
    protected Object doInBackground(String[] revision) {

        Aplicacion.backupBaseDatos();
        Aplicacion.generarXML(revision[0]);
        Aplicacion.generarKML(revision[0]);
        Aplicacion.generarArchivoEquiposTXT(revision[0]);
        Aplicacion.generarArchivoDefectosTXT(revision[0]);

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
