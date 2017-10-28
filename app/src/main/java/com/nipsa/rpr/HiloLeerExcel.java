package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.net.URI;


public class HiloLeerExcel extends AsyncTask<Uri, Object, Object> {

    ProgressDialog pd;

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

    @Override
    protected Object doInBackground(Uri... objects) {
        // TODO: Lanzar lectura del archivo excel en ExcelHandler

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        pd.dismiss();
    }

}
