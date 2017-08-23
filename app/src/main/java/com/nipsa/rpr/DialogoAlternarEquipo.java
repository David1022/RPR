package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class DialogoAlternarEquipo extends DialogFragment {

    private DBRevisiones dbRevisiones;
    private String equipoActual;
    private Vector<Equipo> listaEquipos;
    private ListView listView;
    private AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        dbRevisiones = new DBRevisiones(getContext());
        equipoActual = Aplicacion.equipoActual;
        builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialogo_alternar_equipo, null);

        listaEquipos = dbRevisiones.solicitarListaEquiposIguales(equipoActual);
        listView = (ListView) vista.findViewById(R.id.listAlternarEquipos);
        listView.setAdapter(new DialogoAlternarEquipo.AdaptadorAlternarEquipos(this));

        builder.setView(vista)
                .setCancelable(true)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    class AdaptadorAlternarEquipos extends ArrayAdapter<Equipo> {

        Activity context;

        AdaptadorAlternarEquipos(Fragment context) {
            super(context.getActivity(), R.layout.listitem_alternar_equipo, listaEquipos);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_alternar_equipo, null);
            }

            final Equipo equipo = listaEquipos.elementAt(position);

            LinearLayout linearLayout = (LinearLayout) item.findViewById(R.id.linear_alter);
            TextView tvRevision = (TextView) item.findViewById(R.id.item_alter_revision);
            TextView tvEquipo = (TextView) item.findViewById(R.id.item_alter_equipo);
            TextView tvTramo = (TextView) item.findViewById(R.id.item_alter_codigoTramo);

            tvRevision.setText(equipo.getNombreRevision());
            tvEquipo.setText(equipo.getNombreEquipo());
            tvTramo.setText(equipo.getCodigoTramo());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Aplicacion.revisionActual = equipo.getNombreRevision();
                    Aplicacion.equipoActual = equipo.getNombreEquipo();
                    Aplicacion.tramoActual = equipo.getCodigoTramo();
                    Revision revision = dbRevisiones.solicitarRevision(Aplicacion.revisionActual);
                    Intent intent;
                    if(revision.getEstado().equals(Aplicacion.ESTADO_PENDIENTE)) {
                        intent = new Intent(getContext(), MostrarRevisiones.class);
                        Aplicacion.print("La revisión del equipo seleccionado no está iniciada");
                    } else {
                        intent = new Intent(getContext(), MostrarEquipos.class);
                    }
                    dismiss();
                    getActivity().finish();
                    startActivity(intent);
                }
            });

            return (item);
        }
    }

}