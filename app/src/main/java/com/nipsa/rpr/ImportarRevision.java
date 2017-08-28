package com.nipsa.rpr;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImportarRevision extends AppCompatActivity {

    public final String DIRECTORIO_ENTRADA = "/RPR/Input/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_revision);

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarImportarRevision);
        setSupportActionBar(toolbar);

    }

/*
    public void importarRevision() {
        copiarDBAmemoriaInterna();
        //fusionarDBs();

    }

    private void copiarDBAmemoriaInterna() {
        String timeStamp = Aplicacion.fechaHoraActual();
        String inFileName = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;

        // Ruta fichero interno BD
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Ruta fichero salida BD
            final String directorio = "/data/data/com.nipsa.rpr/databases/" + DBBackup.DATABASE_NAME;

            File d = new File(directorio);
            if (!d.exists()) {
                d.mkdirs();
            }
            // Nombre fichero salida BD
            String outFileName = directorio + "/" + nombreRevision +
                    // "_" + timeStamp +
                    "_DBBackup.sqlite";

            // MostrarRevisiones de la copia
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        } catch (Exception e) {
            Log.e("ErrorRPR: ", e.toString());
        }

    }
*/

}
