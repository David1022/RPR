package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class DialogoDefectosAsociados extends DialogFragment {

    public DBRevisiones dbRevisiones;
    public DBGlobal dbGlobal;
    private String revisionActual, equipoActual, tramoActual;
    public ListView lstListado;
    public Vector<String> defAsociados;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbRevisiones = new DBRevisiones(getContext());
        dbGlobal = new DBGlobal(getContext());
        this.revisionActual = Aplicacion.revisionActual;
        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialogo_defectos_asociados, null);

        defAsociados = dbRevisiones.solicitarDefectosAsociadosPorEquipo
                                            (revisionActual, equipoActual, tramoActual);
        lstListado = (ListView)vista.findViewById(R.id.listDefectosAsociados);
        lstListado.setAdapter(new DialogoDefectosAsociados.AdaptadorDefectosAsociados(this));

        builder.setView(vista)
                .setCancelable(false)
                .setIcon(R.drawable.logo_nipsa)
                .setPositiveButton(R.string.boton_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


        return builder.create();

    }

    class AdaptadorDefectosAsociados extends ArrayAdapter<String> {

        Activity context;

        AdaptadorDefectosAsociados(Fragment context) {
            //super(context.getActivity(), R.layout.listitem_grupo_defectos, defAsociados);
            super(context.getActivity(), R.layout.listitem_defectos_asociados, defAsociados);

            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_defectos_asociados, null);
            }

            String codigo = defAsociados.elementAt(position);
            String descripcion = dbGlobal.solicitarDescripcionPorCodigo(codigo);
            TextView textoCodigo = (TextView) item.findViewById(R.id.itemCodigoDefecto);
            TextView textoDescripcion = (TextView) item.findViewById(R.id.itemDescripcionDefecto);
            textoCodigo.setText(codigo);
            textoDescripcion.setText(descripcion);

            return(item);
        }
    }

}