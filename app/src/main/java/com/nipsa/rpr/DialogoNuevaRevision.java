package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DialogoNuevaRevision extends DialogFragment {

    private final String DIRECTORIO_ENTRADA = "/RPR/Input/";
    private String direccionArchivosEntrada;
    private ListView lstListado;
    private TextView titulo;
    private Vector<File> listaNuevas;
    private DBRevisiones dbRevisiones;
    private DialogFragment dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = this;
        dbRevisiones = new DBRevisiones(getContext());
        direccionArchivosEntrada = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;
        MostrarRevisiones.nombreArchivo = "";

        try {
            listaNuevas = MostrarRevisiones.listarArchivosDirEntrada(direccionArchivosEntrada);
            int i = 0;
            while (i<listaNuevas.size()) {
                String nombreRevision = listaNuevas.elementAt(i).getName();
                nombreRevision = nombreRevision.substring(nombreRevision.lastIndexOf("/") + 1);
                boolean existe = dbRevisiones.existeRevision(nombreRevision);
                if (existe) {
                    listaNuevas.remove(i);
                } else {
                    i++;
                }
            }

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View vista = inflater.inflate(R.layout.dialogo_nueva_revision, null);
            titulo = (TextView) vista.findViewById(R.id.tituloNuevaRevision);
            if (listaNuevas.size() == 0) {
                titulo.setText("No hay revisiones pendientes de incluir");
            }
            lstListado = (ListView)vista.findViewById(R.id.listNuevaRevision);
            lstListado.setAdapter(new DialogoNuevaRevision.AdaptadorNuevaRevision(this));

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(vista);
            return builder.create();

        } catch (Exception e) {
            return null;
        }

    }

    class AdaptadorNuevaRevision extends ArrayAdapter<File> {

        Activity context;

        AdaptadorNuevaRevision(Fragment context) {
            super(context.getActivity(), R.layout.listitem_nueva_revision, listaNuevas);
            this.context = context.getActivity();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_nueva_revision, null);
            }

            String revision = listaNuevas.elementAt(position).getName();
            TextView texto = (TextView) item.findViewById(R.id.itemNuevaRevision);
            texto.setText(revision);
            texto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MostrarRevisiones.nombreArchivo = listaNuevas.elementAt(position).getName();
                    dialog.dismiss();
                }
            });

            return(item);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }

    }

}
