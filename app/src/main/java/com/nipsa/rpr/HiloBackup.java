package com.nipsa.rpr;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class HiloBackup extends AsyncTask<Revision, Object, Object> {

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
    protected Object doInBackground(Revision[] revision) {
        String nombreRevision = revision[0].getNombre();

        Aplicacion.backupBaseDatos();
        Aplicacion.generarBackupRevision(nombreRevision);
        Aplicacion.generarXML(nombreRevision);
        Aplicacion.generarKML(nombreRevision);
        Aplicacion.generarArchivoEquiposTXT(nombreRevision);
        Aplicacion.generarArchivoDefectosTXT(nombreRevision);

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
