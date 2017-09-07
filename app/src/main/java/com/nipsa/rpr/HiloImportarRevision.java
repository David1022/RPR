package com.nipsa.rpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by David.Mendano on 07/09/2017.
 */

public class HiloImportarRevision extends AsyncTask <TextView, Object, Object>{

    ProgressDialog pd;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(MostrarRevisiones.contexto);
        pd.setTitle("Espere por favor");
        pd.setMessage("Leyendo base de datos...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    protected Object doInBackground(TextView[] revision) {
        ImportarRevision.importarRevision(revision[0]);
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
