package com.nipsa.rpr;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class ImportarRevision extends AppCompatActivity {

    private String DIRECTORIO_ENTRADA = "/RPR/Input/";
    private final int RESULT_SELECCIONAR_ARCHIVO = 0;

    private DBBackup dbBackup;
    private DBRevisiones dbRevisiones;

    private TextView titulo;
    private ListView lstListado;
    //private ImportarListener listener;
    private AdaptadorImportar mAdapter;
    private int mSelected;

    private Vector<String> listaAMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_revision);

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarImportarRevision);
        setSupportActionBar(toolbar);

        dbRevisiones = new DBRevisiones(this);
        dbBackup = new DBBackup(this);
        mSelected = -1;
        listaAMostrar = new Vector<String>();
        titulo = (TextView) findViewById(R.id.texto_seleccionar_archivo);

        lstListado = (ListView) findViewById(R.id.listImportarRevision);
        Button botonSeleccionarArchivo = (Button) findViewById(R.id.botonSeleccionarArchivo);
        mAdapter = new AdaptadorImportar(this);
        lstListado.setAdapter(mAdapter);

        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                mSelected = pos;
                mAdapter.notifyDataSetChanged();
                TextView revision = (TextView) view.findViewById(R.id.itemImportarRevision);
                dbRevisiones.importarRevision(revision.getText().toString());
                finish();
            }
        });

        botonSeleccionarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, RESULT_SELECCIONAR_ARCHIVO);
            }
        });

    }

    /**
     * Se tratará el archivo seleccionado para importar una revisión
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SELECCIONAR_ARCHIVO) {
            if(resultCode == RESULT_OK) {
                listaAMostrar.setSize(0);
                mSelected = -1;
                mAdapter.notifyDataSetChanged();
                if(data != null){
                    Uri uri = data.getData();
                    if (esSQLite(uri)) {
                        try {
                            InputStream is = getContentResolver().openInputStream(uri);
                            copiarDBAMemoriaInterna(is);
                            listaAMostrar.setSize(0);
                            listaAMostrar.addAll(dbBackup.solicitarListaRevisiones());
                            if((listaAMostrar == null) || (listaAMostrar.size()==0)) {
                                titulo.setText(getResources().getString(R.string.texto_no_revisiones_a_importar));
                            } else {
                                titulo.setText(getResources().getString(R.string.titulo_importar_revision));
                            }
                            mSelected = -1;
                            mAdapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            Log.e(Aplicacion.TAG, "Error al leer el archivo: " + e.toString());
                        }
                    } else {
                        Aplicacion.print("Tipo de archivo no válido. Selecciona un archivo con " +
                                "extensión *.SQLITE");
                    }
                }
            }
        }
    }

    /**
     * Comprueba que el archivo pasado por parámetro tenga extensión .sqlite
     * @param uri
     * @return
     */
    private boolean esSQLite (Uri uri) {
        boolean esSQLite = false;
        String s = uri.toString();
        if(s.contains(".")){
            s = s.substring(s.lastIndexOf("."));
            esSQLite = s.equalsIgnoreCase(".sqlite");
        }

        return esSQLite;
    }

    /**
     * Copia el fichero que contiene la BDD a la memoria interna para poder ser gestionado
     * @param fis
     */
    private void copiarDBAMemoriaInterna(InputStream fis) {
        String timeStamp = Aplicacion.fechaHoraActual();
        String inFileName = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;

        // Ruta fichero interno BD
        try {
            File dbFile = new File(inFileName);

            // Ruta fichero salida BD
            //String directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
            //                        DIRECTORIO_SALIDA;
            final String directorio = "/data/data/com.nipsa.rpr/databases/";

            File d = new File(directorio);
            if (!d.exists()) {
                d.mkdirs();
            }
            // Nombre fichero salida BD
            String outFileName = directorio + DBBackup.DATABASE_NAME;

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

/*
    public void onImportarSeleccionado(String s) {
        importarRevision(s);
    }

    public interface ImportarListener {
        void onImportarSeleccionado(String s);
    }

    public void setImportarListener(ImportarRevision.ImportarListener listener) {
        this.listener=listener;
    }
*/

    class AdaptadorImportar extends ArrayAdapter<String> {

        Activity context;

        AdaptadorImportar(Activity context) {
            super(context, R.layout.listitem_importar_revision, listaAMostrar);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_importar_revision, null);
            }

            // Se cambia el color de background si está seleccionado
            if (position == mSelected) {
                item.setBackgroundColor(getResources().getColor(R.color.background_selected_listview));

            } else {
                item.setBackgroundColor(getResources().getColor(R.color.blanco));
            }

            TextView textNombre = (TextView)item.findViewById(R.id.itemImportarRevision);
            String revision = listaAMostrar.get(position);

            textNombre.setText(revision);

            return(item);
        }

    }

}
